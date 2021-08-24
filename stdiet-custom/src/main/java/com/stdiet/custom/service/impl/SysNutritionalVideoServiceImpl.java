package com.stdiet.custom.service.impl;

import com.aliyun.vod20170321.models.SearchMediaResponse;
import com.aliyun.vod20170321.models.SearchMediaResponseBody;
import com.stdiet.common.utils.AliyunVideoUtils;
import com.stdiet.common.utils.DateUtils;
import com.stdiet.common.utils.StringUtils;
import com.stdiet.common.utils.oss.AliyunOSSUtils;
import com.stdiet.custom.domain.SysNutritionalVideo;
import com.stdiet.custom.domain.SysVideoComment;
import com.stdiet.custom.mapper.SysNutritionalVideoMapper;
import com.stdiet.custom.mapper.SysVideoCommentMapper;
import com.stdiet.custom.service.ISysNutritionalVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 营养视频Service业务层处理
 *
 * @author xzj
 * @date 2021-04-29
 */
@Service
public class SysNutritionalVideoServiceImpl implements ISysNutritionalVideoService {
    @Autowired
    private SysNutritionalVideoMapper sysNutritionalVideoMapper;

    @Autowired
    private SysVideoCommentMapper sysVideoCommentMapper;

    /**
     * 查询营养视频
     *
     * @param id 营养视频ID
     * @return 营养视频
     */
    @Override
    public SysNutritionalVideo selectSysNutritionalVideoById(Long id) {
        return sysNutritionalVideoMapper.selectSysNutritionalVideoById(id);
    }

    /**
     * 查询营养视频列表
     *
     * @param sysNutritionalVideo 营养视频
     * @return 营养视频
     */
    @Override
    public List<SysNutritionalVideo> selectSysNutritionalVideoList(SysNutritionalVideo sysNutritionalVideo, boolean flag) {
        List<SysNutritionalVideo> list = sysNutritionalVideoMapper.selectSysNutritionalVideoList(sysNutritionalVideo);
        if (flag && list != null && list.size() > 0) {
            List<String> fileUrl = new ArrayList<>();
            List<String> videoIdList = new ArrayList<>();
            for (SysNutritionalVideo video : list) {
                if (StringUtils.isNotEmpty(video.getCoverUrl())) {
                    fileUrl.add(video.getCoverUrl());
                } else {
                    videoIdList.add(video.getVideoId());
                }
            }
            if (fileUrl.size() > 0) {
                List<String> downUrlList = AliyunOSSUtils.generatePresignedUrl(fileUrl);
                if (downUrlList != null && downUrlList.size() > 0) {
                    int index = 0;
                    for (SysNutritionalVideo video : list) {
                        if (StringUtils.isNotEmpty(video.getCoverUrl())) {
                            video.setCoverUrl(downUrlList.get(index));
                            index++;
                            if (index == downUrlList.size()) {
                                break;
                            }
                        }
                    }
                }
            }
            if (videoIdList.size() > 0) {
                List<String> coverUrlList = AliyunVideoUtils.getVideoCoverUrl(videoIdList);
                if (coverUrlList != null && coverUrlList.size() > 0) {
                    int index = 0;
                    for (SysNutritionalVideo video : list) {
                        if (StringUtils.isEmpty(video.getCoverUrl())) {
                            video.setCoverUrl(coverUrlList.get(index));
                            index++;
                            if (index == coverUrlList.size()) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 新增营养视频
     *
     * @param sysNutritionalVideo 营养视频
     * @return 结果
     */
    @Override
    public int insertSysNutritionalVideo(SysNutritionalVideo sysNutritionalVideo) {
        sysNutritionalVideo.setCreateTime(DateUtils.getNowDate());
        //判断封面是上传的还是阿里云视频截图封面
        if (isSnapshot(sysNutritionalVideo.getCoverUrl())) {
            //更新阿里云视频封面
            try {
                AliyunVideoUtils.updateVideoCoverUrl(sysNutritionalVideo.getVideoId(), sysNutritionalVideo.getCoverUrl());
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            sysNutritionalVideo.setCoverUrl("");
        }
        return sysNutritionalVideoMapper.insertSysNutritionalVideo(sysNutritionalVideo);
    }

    /**
     * 修改营养视频
     *
     * @param sysNutritionalVideo 营养视频
     * @return 结果
     */
    @Override
    public int updateSysNutritionalVideo(SysNutritionalVideo sysNutritionalVideo) {
        sysNutritionalVideo.setUpdateTime(DateUtils.getNowDate());
        sysNutritionalVideo.setCoverUrl(sysNutritionalVideo.getCoverUrl() == null ? "" : sysNutritionalVideo.getCoverUrl());
        String coverUrl = sysNutritionalVideo.getCoverUrl();
        //判断封面是上传的还是阿里云视频截图封面
        sysNutritionalVideo.setCoverUrl(isSnapshot(coverUrl) ? "" : coverUrl);
        int row = sysNutritionalVideoMapper.updateSysNutritionalVideo(sysNutritionalVideo);
        if (row > 0) {
            sysNutritionalVideo.setCoverUrl(isSnapshot(coverUrl) ? coverUrl : null);
            updateAliyunVideo(sysNutritionalVideo);
        }
        return row;
    }

    @Async
    public void updateAliyunVideo(SysNutritionalVideo sysNutritionalVideo) {
        try {
            if (sysNutritionalVideo != null && sysNutritionalVideo.getVideoId() != null) {
                AliyunVideoUtils.updateVideo(sysNutritionalVideo.getVideoId(), sysNutritionalVideo.getTitle(), sysNutritionalVideo.getTags(), sysNutritionalVideo.getDescription(), null, sysNutritionalVideo.getCoverUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除营养视频
     *
     * @param ids 需要删除的营养视频ID
     * @return 结果
     */
    @Override
    public int deleteSysNutritionalVideoByIds(Long[] ids) {
        int row = sysNutritionalVideoMapper.deleteSysNutritionalVideoByIds(ids);
        if (row > 0) {
            updateAliyunVideoCateId(ids);
        }
        return row;
    }

    /**
     * 删除营养视频信息
     *
     * @param id 营养视频ID
     * @return 结果
     */
    @Override
    public int deleteSysNutritionalVideoById(Long id) {
        int row = sysNutritionalVideoMapper.deleteSysNutritionalVideoById(id);
        if (row > 0) {
            Long[] ids = {id};
            updateAliyunVideoCateId(ids);
        }
        return row;
    }

    /**
     * 获取视频
     *
     * @param videoId
     * @return
     */
    public SysNutritionalVideo selectSysNutritionalVideByVideoId(String videoId) {
        return sysNutritionalVideoMapper.selectSysNutritionalVideByVideoId(videoId);
    }

    /**
     * 阿里云视频查询检索
     *
     * @return
     */
    public Map<String, Object> searchVideo(String key, Integer showFlag, Integer pageNo, Integer pageSize, String scrollToken) {
        pageSize = pageSize.intValue() > 100 ? 10 : pageSize;
        long total = 0;
        String newScrollToken = null;
        List<SysNutritionalVideo> nutritionalVideoList = new ArrayList<>();
        try {
            SearchMediaResponse response = AliyunVideoUtils.searchVideo(key, getStatusString(showFlag), pageNo, pageSize, scrollToken);
            if (response != null) {
                SearchMediaResponseBody body = response.body;
                total = body.total;
                newScrollToken = body.scrollToken;
                List<SearchMediaResponseBody.SearchMediaResponseBodyMediaList> mediaList = body.mediaList;
                if (mediaList != null && mediaList.size() > 0) {
                    for (SearchMediaResponseBody.SearchMediaResponseBodyMediaList media : mediaList) {
                        SysNutritionalVideo sysNutritionalVideo = new SysNutritionalVideo();
                        sysNutritionalVideo.setTitle(media.video.title);
                        sysNutritionalVideo.setCoverUrl(media.video.coverURL);
                        sysNutritionalVideo.setShowFlag(getStatus(media.video.getStatus()));
                        sysNutritionalVideo.setTags(media.video.tags);
                        //String createTime = media.video.creationTime;
                        sysNutritionalVideo.setDescription(media.video.description);
                        sysNutritionalVideo.setVideoId(media.video.videoId);
                        nutritionalVideoList.add(sysNutritionalVideo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("newScrollToken", newScrollToken);
        result.put("nutritionalVideoList", nutritionalVideoList);
        return result;
    }

    private String getStatusString(Integer status) {
        if (status == null) {
            return "Normal,Blocked";
        }
        return status.intValue() == 1 ? "Normal" : "Blocked";
    }

    private Integer getStatus(String status) {
        if (status == null) {
            return 1;
        }
        return "Normal".equals(status) ? 1 : 0;
    }

    /**
     * 更新微信展示状态
     *
     * @param wxShow
     * @param ids
     * @return
     */
    public int updateWxshowByIds(Integer wxShow, Long[] ids) {
        return sysNutritionalVideoMapper.updateWxshowByIds(wxShow, ids);
    }

    /**
     * 将删除的阿里云视频放入回收站
     *
     * @param ids
     */
    @Async
    public void updateAliyunVideoCateId(Long[] ids) {
        try {
            List<String> videoIdList = sysNutritionalVideoMapper.getVideoIdByIds(ids);
            if (videoIdList != null && videoIdList.size() > 0) {
                for (String videoId : videoIdList) {
                    AliyunVideoUtils.delVideo(videoId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新视频播放量
     *
     * @return
     */
    public int updateVideoPlayNum(String videoId) {
        return sysNutritionalVideoMapper.updateVideoPlayNum(videoId);
    }

    @Override
    public List<SysVideoComment> selectVideoCommentByTopicId(SysVideoComment videoComment, String uid) {
        List<SysVideoComment> comments = sysVideoCommentMapper.selectVideoCommentByTopicId(videoComment);
        for (SysVideoComment comment : comments) {
            if(comment.getFromUid().equals(uid)){
                comment.setOwner(true);
            } else {
                comment.setOwner(false);
            }
            if (comment.getFromRole().equals("phone")) {
                comment.setFromUid(StringUtils.hiddenPhoneNumber(comment.getFromUid()));
                comment.setFromName(StringUtils.hiddenPhoneNumber(comment.getFromName()));
            }
            if (comment.getToRole().equals("phone")) {
                comment.setToUid(StringUtils.hiddenPhoneNumber(comment.getToUid()));
                comment.setToName(StringUtils.hiddenPhoneNumber(comment.getToName()));
            }
            List<SysVideoComment> replys = comment.getReplys();
            for (SysVideoComment reply : replys) {
                if(reply.getFromUid().equals(uid)){
                    reply.setOwner(true);
                } else {
                    reply.setOwner(false);
                }
                if (reply.getFromRole().equals("phone")) {
                    reply.setFromUid(StringUtils.hiddenPhoneNumber(reply.getFromUid()));
                    reply.setFromName(StringUtils.hiddenPhoneNumber(reply.getFromName()));
                }
                if (reply.getToRole().equals("phone")) {
                    reply.setToUid(StringUtils.hiddenPhoneNumber(reply.getToUid()));
                    reply.setToName(StringUtils.hiddenPhoneNumber(reply.getToName()));
                }
            }
        }
        return comments;
    }

    @Override
    public SysVideoComment insertVideoComment(SysVideoComment videoComment) {
        String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
        videoComment.setId(uuid);
        int rows = sysVideoCommentMapper.insertVideoComment(videoComment);
        if (rows > 0) {
            return videoComment;
        }
        return null;
    }

    @Override
    public SysVideoComment insertVideoCommentReply(SysVideoComment videoComment) {
        String id = java.util.UUID.randomUUID().toString().replace("-", "");
        videoComment.setId(id);
        String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
        videoComment.setReplyId(uuid);
        int rows = sysVideoCommentMapper.insertVideoCommentReply(videoComment);
        if (rows > 0) {
            return videoComment;
        }
        return null;
    }

    @Override
    public int deleteVideoCommentById(String id) {
        return sysVideoCommentMapper.deleteVideoCommentById(id);
    }

    @Override
    public int deleteVideoCommentReplyById(String id) {
        return sysVideoCommentMapper.deleteVideoCommentReplyById(id);
    }

    /**
     * 判断是否为阿里点播的截图
     *
     * @param url
     * @return
     */
    private boolean isSnapshot(String url) {
        return StringUtils.isNotEmpty(url) && url.startsWith("http://outin");
    }


}