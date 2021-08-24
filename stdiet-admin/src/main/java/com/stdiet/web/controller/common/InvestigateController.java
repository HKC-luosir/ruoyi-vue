package com.stdiet.web.controller.common;

import com.stdiet.common.core.controller.BaseController;
import com.stdiet.common.core.domain.AjaxResult;
import com.stdiet.common.core.page.TableDataInfo;
import com.stdiet.common.enums.BusinessType;
import com.stdiet.common.utils.HealthyUtils;
import com.stdiet.common.utils.StringUtils;
import com.stdiet.common.utils.sign.AesUtils;
import com.stdiet.common.utils.uuid.IdUtils;
import com.stdiet.custom.domain.*;
import com.stdiet.custom.dto.request.CustomerInvestigateRequest;
import com.stdiet.custom.dto.request.FoodHeatCalculatorRequest;
import com.stdiet.custom.service.*;
import com.stdiet.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 客户相关信息调查Controller
 *
 * @author xzj
 * @date 2020-12-31
 */
@RestController
@RequestMapping("/investigate")
public class InvestigateController extends BaseController {

    @Autowired
    private ISysCustomerPhysicalSignsService sysCustomerPhysicalSignsService;

    @Autowired
    private ISysPhysicalSignsService iSysPhysicalSignsService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysCustomerHealthyService sysCustomerHealthyService;

    @Autowired
    private ISysCustomerService sysCustomerService;

    @Autowired
    private ISysFoodHeatStatisticsService sysFoodHeatStatisticsService;

    @Autowired
    private ISysOrderService sysOrderService;

    @Autowired
    private ISysPreSaleSurveyService sysPreSaleSurveyService;

    /**
     * 建立客户信息档案
     */
    @PostMapping("/customerInvestigate")
    public AjaxResult customerInvestigate(@RequestBody CustomerInvestigateRequest customerInvestigateRequest) throws Exception
    {
        return AjaxResult.error("请填写新版健康评估表");
        //customerInvestigateRequest.setId(null); //只能添加，无法修改
        //return sysCustomerPhysicalSignsService.addOrupdateCustomerAndSign(customerInvestigateRequest);
    }

    /**
     * 获取体征列表
     */
    @GetMapping("/physicalSignsList")
    public TableDataInfo physicalSignsList() throws Exception
    {
        List<SysPhysicalSigns>  physicalSignsList =  iSysPhysicalSignsService.selectSysPhysicalSignsList(new SysPhysicalSigns());
        //System.out.println(physicalSignsList.size());
        return getDataTable(physicalSignsList);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        return AjaxResult.success(dictTypeService.selectDictDataByType(dictType));
    }

    /**
     * 根据加密ID获取客户基本信息
     * @param enc_id
     * @return
     */
    @GetMapping("/getCustomerBaseMessage/{id}")
    public AjaxResult getCustomerBaseMessage(@PathVariable(value = "id") String enc_id){
        String id = StringUtils.isEmpty(enc_id) ? "" : AesUtils.decrypt(enc_id, null);
        if(StringUtils.isNotEmpty(id)){
            SysCustomer sysCustomer = sysCustomerService.selectSysCustomerById(Long.parseLong(id));
            if(sysCustomer != null){
                Map<String, Object> result = new HashMap<>();
                result.put("name", sysCustomer.getName());
                result.put("phone", sysCustomer.getPhone());
                //查询下单对应调理项目
                SysOrder order = sysOrderService.getLastOrderByCusId(Long.parseLong(id));
                result.put("projectId", (order != null && order.getConditioningProjectId() != null) ? order.getConditioningProjectId() : null);
                return AjaxResult.success(result);
            }
        }
        return AjaxResult.success();
    }

    /**
     * 新增客户健康
     */
    @PostMapping("/addCustomerHealthy")
    public AjaxResult addCustomerHealthy(@RequestBody SysCustomerHealthy sysCustomerHealthy)
    {
        return sysCustomerHealthyService.insertSysCustomerHealthy(sysCustomerHealthy);
    }

    /**
     * 新增客户外食计算统计
     */
    @PostMapping("/addFoodHeatStatistics")
    public AjaxResult addFoodHeatStatistics(@RequestBody FoodHeatCalculatorRequest foodHeatCalculatorRequest)
    {
        return toAjax(sysFoodHeatStatisticsService.addMuchFoodHeat(foodHeatCalculatorRequest));
    }

    /**
     * 添加用户基础信息问卷调查表
     */
    @PostMapping(value = "/addCustomerSurvey")
    public AjaxResult addCustomerSurvey(@RequestBody SysPreSaleSurvey sysPreSaleSurvey)
    {
        //判断客户唯一标识是否为空
        if(StringUtils.isEmpty(sysPreSaleSurvey.getCustomerKey())){
            return AjaxResult.error("操作失败，缺少必要参数");
        }
        SysPreSaleSurvey oldSysPreSale = sysPreSaleSurveyService.getSysPreSaleSurveyByKey(sysPreSaleSurvey.getCustomerKey());
        if(oldSysPreSale != null){
            return AjaxResult.error("已提交过问卷，无法重复提交");
        }
        sysPreSaleSurvey.setQuestionType(0);
        return toAjax(sysPreSaleSurveyService.insertSysPreSaleSurvey(sysPreSaleSurvey));
    }

    /**
     * 根据客户唯一标识
     */
    @GetMapping("/getCustomerSurvey/{customerKey}")
    public AjaxResult getCustomerSurvey(@PathVariable("customerKey")String customerKey)
    {
        //判断客户唯一标识是否为空
        if(StringUtils.isEmpty(customerKey)){
            return AjaxResult.error("操作失败，缺少必要参数");
        }
        SysPreSaleSurvey preSaleSurvey = sysPreSaleSurveyService.getSysPreSaleSurveyByKey(customerKey);
        return AjaxResult.success(preSaleSurvey);
    }

    /**
     * 添加用户简易问卷
     */
    @PostMapping("/addSimpleCustomerSurvey")
    public AjaxResult addSimpleCustomerSurvey(@RequestBody SysPreSaleSurvey sysPreSaleSurvey)
    {
        sysPreSaleSurvey.setQuestionType(1);
        sysPreSaleSurvey.setCustomerKey(IdUtils.fastSimpleUUID());
        int row = sysPreSaleSurveyService.insertSysPreSaleSurvey(sysPreSaleSurvey);
        if(row > 0){
            //获取标准体重
            Double standardWeight = HealthyUtils.calculateStandardWeightByBMI(sysPreSaleSurvey.getTall(), sysPreSaleSurvey.getAge(), sysPreSaleSurvey.getSex());
            return AjaxResult.success(standardWeight);
        }
        return AjaxResult.error("提交失败");
    }
}