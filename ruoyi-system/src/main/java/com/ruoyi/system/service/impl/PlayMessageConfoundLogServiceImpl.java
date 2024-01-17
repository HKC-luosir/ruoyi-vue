package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.PlayConstants;
import com.ruoyi.common.tools.UrlReplaceTools;
import com.ruoyi.common.utils.ListTools;
import com.ruoyi.system.callback.dto.CalledDTO;
import com.ruoyi.system.domain.PlayMessageConfound;
import com.ruoyi.system.domain.PlayMessageConfoundLog;
import com.ruoyi.system.domain.data.Callback1100850405DATA;
import com.ruoyi.system.domain.data.Callback1100850508DATA;
import com.ruoyi.system.domain.data.InsertEventOutputDTO;
import com.ruoyi.system.domain.dto.ConfoundRetryDTO;
import com.ruoyi.system.domain.dto.QueryConfoundLogDTO;
import com.ruoyi.system.domain.vo.QueryConfoundLogVO;
import com.ruoyi.system.mapper.PlayMessageConfoundLogMapper;
import com.ruoyi.system.openapi.OpenApiClient;
import com.ruoyi.system.openapi.OpenApiResult;
import com.ruoyi.system.openapi.model.input.ThirdTgAppointGradeTextListInputDTO;
import com.ruoyi.system.openapi.model.input.ThirdTgDisperseImageInputDTO;
import com.ruoyi.system.openapi.model.input.ThirdTgInsertEventInputDTO;
import com.ruoyi.system.openapi.model.output.TgBaseOutputDTO;
import com.ruoyi.system.service.PlayMessageConfoundLogService;
import com.ruoyi.system.service.PlayMessageConfoundService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@Slf4j
public class PlayMessageConfoundLogServiceImpl extends ServiceImpl<PlayMessageConfoundLogMapper, PlayMessageConfoundLog>
        implements PlayMessageConfoundLogService {

    private static int IMG_CONFOUND_SIZE = 5;

    @Resource
    private PlayMessageConfoundService playMessageConfoundService;


    @Override
    public void removeByConfoundIds(List<Integer> confoundIds) {
        if (CollectionUtils.isEmpty(confoundIds)) {
            return;
        }

        LambdaQueryWrapper<PlayMessageConfoundLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PlayMessageConfoundLog::getMessageConfoundId, confoundIds);
        super.remove(lambdaQueryWrapper);
    }

    @Override
    public void createConfoundLog(List<PlayMessageConfound> confoundList) {

        for (PlayMessageConfound confound : confoundList) {
            List<PlayMessageConfoundLog> logList = new ArrayList<>();
            // 图片要按群数量%10 多次获取
            if (confound.getMomentTypeId() == 2002) {
                int batchSize = confound.getGroupNum() % IMG_CONFOUND_SIZE == 0 ?
                        confound.getGroupNum() / IMG_CONFOUND_SIZE : (confound.getGroupNum() / IMG_CONFOUND_SIZE + 1);
                for (int i = 0; i < batchSize; i++) {
                    PlayMessageConfoundLog confoundLog = new PlayMessageConfoundLog();
                    confoundLog.setMessageConfoundId(confound.getId());
                    confoundLog.setState(0);

                    String optSerNo = this.disperseImage(confound.getConfoundContent());
                    confoundLog.setOptSerialNo(optSerNo);
                    if (StringUtils.isEmpty(optSerNo)) { //失败
                        confoundLog.setState(2);
                        confoundLog.setFailMessage("请求失败");
                    }
                    logList.add(confoundLog);
                }
            } else if (confound.getMomentTypeId() == 2005) {
                // 链接 按群数量循环同步调用
                for (Integer i = 0; i < confound.getGroupNum(); i++) {
                    PlayMessageConfoundLog confoundLog = new PlayMessageConfoundLog();
                    confoundLog.setMessageConfoundId(confound.getId());
                    confoundLog.setState(0);

                    TgBaseOutputDTO<InsertEventOutputDTO> outputDTO = this.insertEvent(confound.getConfoundContent());
                    String optSerNo = Optional.ofNullable(outputDTO)
                            .map(TgBaseOutputDTO::getOptSerNo)
                            .orElse("");
                    confoundLog.setOptSerialNo(optSerNo);

                    String resultContent = Optional.ofNullable(outputDTO)
                            .map(TgBaseOutputDTO::getData)
                            .map(InsertEventOutputDTO::getActivityLinkAddress)
                            .orElse("");
                    confoundLog.setResultContent(resultContent);
                    if (StringUtils.isEmpty(resultContent)) { //失败
                        confoundLog.setState(2);
                        confoundLog.setFailMessage("请求失败");
                    } else {
                        confoundLog.setState(1);
                        confoundLog.setFailMessage("");
                    }
                    logList.add(confoundLog);
                }
            } else {
                PlayMessageConfoundLog confoundLog = new PlayMessageConfoundLog();
                confoundLog.setMessageConfoundId(confound.getId());
                confoundLog.setState(0);

                String optSerNo = this.getAppointGradeTextList(confound.getConfoundContent(), confound.getGroupNum());
                confoundLog.setOptSerialNo(optSerNo);
                if (StringUtils.isEmpty(optSerNo)) { //失败
                    confoundLog.setState(2);
                    confoundLog.setFailMessage("请求失败");
                }
                logList.add(confoundLog);
            }
            super.saveBatch(logList);
        }

    }

    public PlayMessageConfoundLog getConfoundLogByOptSerNo(String optSerNo) {
        if (StringUtils.isEmpty(optSerNo)) {
            return null;
        }
        LambdaQueryWrapper<PlayMessageConfoundLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PlayMessageConfoundLog::getOptSerialNo, optSerNo);
        lambdaQueryWrapper.orderByDesc(PlayMessageConfoundLog::getId);
        lambdaQueryWrapper.last("limit 1");
        return super.getOne(lambdaQueryWrapper);
    }


    @Override
    public void handleConfoundText(CalledDTO inputDTO) {
        PlayMessageConfoundLog confoundLog = this.getConfoundLogByOptSerNo(inputDTO.getOptSerNo());
        if (confoundLog == null) {
            log.info("handleConfoundText confoundLog is null {}", inputDTO.getOptSerNo());
            return;
        }
        Callback1100850405DATA data = JSONObject.parseObject(inputDTO.getData().toString(), Callback1100850405DATA.class);
        if (data == null || CollectionUtils.isEmpty(data.getDiscrete_list())) {
            confoundLog.setState(2);
            confoundLog.setFailMessage("回调无结果");
        } else {
            confoundLog.setState(1);
            confoundLog.setFailMessage("");
            String joiningRegex = PlayConstants.joiningRegex;
            String resultContent = data.getDiscrete_list().stream()
                    .collect(Collectors.joining(joiningRegex));
            confoundLog.setResultContent(resultContent);
        }
        super.updateById(confoundLog);

    }

    @Override
    public void handleConfoundImg(CalledDTO inputDTO) {
        PlayMessageConfoundLog confoundLog = this.getConfoundLogByOptSerNo(inputDTO.getOptSerNo());
        if (confoundLog == null) {
            log.info("handleConfoundImg confoundLog is null {}", inputDTO.getOptSerNo());
            return;
        }
        Callback1100850508DATA data = JSONObject.parseObject(inputDTO.getData().toString(), Callback1100850508DATA.class);
        if (data == null || CollectionUtils.isEmpty(data.getDiscrete_list())) {
            confoundLog.setState(2);
            confoundLog.setFailMessage("回调无结果");
        } else {
            confoundLog.setState(1);
            confoundLog.setFailMessage("");
            String joiningRegex = PlayConstants.joiningRegex;
            List<String> contentList = new ArrayList<>();

            for (String discrete : data.getDiscrete_list()) {
                String replace = "";
                int batchNum = 0;
                do {
                    batchNum++;
                    replace = UrlReplaceTools.replace(discrete);
                    log.info("handleConfoundImg replace [{}] [{}] [{}]", inputDTO.getOptSerNo(), discrete, replace);
                } while (StringUtils.isEmpty(replace) && batchNum < 3);
                if (StringUtils.isNotBlank(replace)) {
                    contentList.add(replace);
                }
            }
            String resultContent = contentList.stream()
                    .collect(Collectors.joining(joiningRegex));
            confoundLog.setResultContent(resultContent);
        }
        super.updateById(confoundLog);
    }


    public String disperseImage(String imageUrl) {
        ThirdTgDisperseImageInputDTO dto = new ThirdTgDisperseImageInputDTO();
        dto.setImageUrl(imageUrl);
        dto.setNum(IMG_CONFOUND_SIZE);

        OpenApiResult<TgBaseOutputDTO> result = OpenApiClient.disperseImageByThirdKpTg(dto);

        log.info("PlayMessageConfoundLogServiceImpl disperseImage {} {}", JSON.toJSONString(dto), JSON.toJSONString(result));
        return Optional.ofNullable(result)
                .map(OpenApiResult::getData)
                .map(TgBaseOutputDTO::getOptSerNo)
                .orElse("");
    }

    /**
     * 获取离散文字
     *
     * @param content
     * @param num     返回多少条
     * @return
     */
    public String getAppointGradeTextList(String content, Integer num) {
        ThirdTgAppointGradeTextListInputDTO dto = new ThirdTgAppointGradeTextListInputDTO();
        dto.setTextContent(content);

        // 获取条数，各离散等级条数限制 L1： 10 L2： 100 L3： 500 L4-L5： 1000
        dto.setLevel(5);
        dto.setNum(num);

        OpenApiResult<TgBaseOutputDTO> result = OpenApiClient.getAppointGradeTextListByThirdKpTg(dto);

        log.info("PlayMessageConfoundLogServiceImpl getAppointGradeTextList {} {}", JSON.toJSONString(dto), JSON.toJSONString(result));
        return Optional.ofNullable(result)
                .map(OpenApiResult::getData)
                .map(TgBaseOutputDTO::getOptSerNo)
                .orElse("");
    }

    public TgBaseOutputDTO<InsertEventOutputDTO> insertEvent(String activityRule) {
        ThirdTgInsertEventInputDTO dto = new ThirdTgInsertEventInputDTO();
        dto.setActivityRule(activityRule);
        //域名分组传10
        dto.setDomainGroupTypeId(PlayConstants.domainGroupTypeId);
        //10：跳转落地URL 20：直接跳转原始URL
        dto.setRedirectWay(PlayConstants.redirectWa);

        OpenApiResult<TgBaseOutputDTO> result = OpenApiClient.insertEventByThirdKpTg(dto);
        log.info("PlayMessageConfoundLogServiceImpl insertEvent {} {}", JSON.toJSONString(dto), JSON.toJSONString(result));
        TgBaseOutputDTO outputDTO = result.getData();
        Object object = Optional.ofNullable(outputDTO)
                .map(TgBaseOutputDTO::getData)
                .orElse(null);
        if (object != null) {
            outputDTO.setData(JSONObject.parseObject(object.toString(), InsertEventOutputDTO.class));
        }
        return outputDTO;
    }

    @Override
    public void retryingConfusion(String lockKey) {
        try {
            // 扫描失败 且 重试次数小于3得数据
            List<PlayMessageConfoundLog> logList = baseMapper.selectRetryingList();
            if (CollectionUtils.isEmpty(logList)) {
                return;
            }
            Map<Integer, PlayMessageConfound> confoundMap = new HashMap<>();
            for (PlayMessageConfoundLog confoundLog : logList) {
                PlayMessageConfound confound = confoundMap.get(confoundLog.getMessageConfoundId());
                if (confound == null) {
                    confound = playMessageConfoundService.getById(confoundLog.getMessageConfoundId());
                    if (confound == null) {
                        continue;
                    }
                    confoundMap.put(confound.getId(), confound);
                }
                this.retryingConfusion(confoundLog, confound);
            }
        } catch (Exception e) {
            log.info("retryingConfusion error", e);
        } finally {
//            ClusterLock.of().unlock(lockKey, lockKey);
        }
    }

    private void retryingConfusion(PlayMessageConfoundLog confoundLog, PlayMessageConfound confound) {
        if (confound.getMomentTypeId() == 2001) {
            String optSerNo = this.getAppointGradeTextList(confound.getConfoundContent(), confound.getGroupNum());
            confoundLog.setOptSerialNo(optSerNo);
            if (StringUtils.isEmpty(optSerNo)) {
                confoundLog.setState(2);
                confoundLog.setFailMessage("请求失败");
            } else {
                confoundLog.setState(3);
                confoundLog.setFailMessage("重试中");
            }
        } else if (confound.getMomentTypeId() == 2002) {
            String optSerNo = this.disperseImage(confound.getConfoundContent());
            confoundLog.setOptSerialNo(optSerNo);
            if (StringUtils.isEmpty(optSerNo)) {
                confoundLog.setState(2);
                confoundLog.setFailMessage("请求失败");
            } else {
                confoundLog.setState(3);
                confoundLog.setFailMessage("重试中");
            }
        } else if (confound.getMomentTypeId() == 2005) {
            TgBaseOutputDTO<InsertEventOutputDTO> outputDTO = this.insertEvent(confound.getConfoundContent());
            String optSerNo = Optional.ofNullable(outputDTO)
                    .map(TgBaseOutputDTO::getOptSerNo)
                    .orElse("");
            confoundLog.setOptSerialNo(optSerNo);
            String resultContent = Optional.ofNullable(outputDTO)
                    .map(TgBaseOutputDTO::getData)
                    .map(InsertEventOutputDTO::getActivityLinkAddress)
                    .orElse("");
            confoundLog.setResultContent(resultContent);
            if (StringUtils.isEmpty(resultContent)) { //失败
                confoundLog.setState(2);
                confoundLog.setFailMessage("请求失败");
            } else {
                confoundLog.setState(1);
                confoundLog.setFailMessage("");
            }
        } else {
            confoundLog.setOptSerialNo("");
            confoundLog.setState(2);
            confoundLog.setFailMessage("未支持的混淆类型");
        }
        confoundLog.setExecuteNum(confoundLog.getExecuteNum() + 1);
        super.updateById(confoundLog);
    }

    @Override
    public Page<QueryConfoundLogVO> page(QueryConfoundLogDTO dto) {
        Page<QueryConfoundLogVO> page = new Page<>(dto.getPage(), dto.getLimit());
        baseMapper.page(page, dto);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            String splitRegex = PlayConstants.splitRegex;
            for (QueryConfoundLogVO record : page.getRecords()) {
                if (StringUtils.isEmpty(record.getResultContent())) {
                    continue;
                }
                List<String> resultList = ListTools.newArrayList(record.getResultContent().split(splitRegex));
                record.setResultList(resultList);
            }
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retry(ConfoundRetryDTO dto) {
        if (CollectionUtils.isEmpty(dto.getIds())) {
            return;
        }
        List<PlayMessageConfoundLog> confoundLogs = super.listByIds(dto.getIds());
        Map<Integer, PlayMessageConfound> confoundMap = new HashMap<>();
        for (PlayMessageConfoundLog confoundLog : confoundLogs) {
            PlayMessageConfound confound = confoundMap.get(confoundLog.getMessageConfoundId());
            if (confound == null) {
                confound = playMessageConfoundService.getById(confoundLog.getMessageConfoundId());
                if (confound == null) {
                    continue;
                }
                confoundMap.put(confound.getId(), confound);
            }
//            this.retryingConfusion(confoundLog, confound);
        }
    }
}




