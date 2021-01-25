package com.stdiet.web.controller.common;

import com.stdiet.common.core.controller.BaseController;
import com.stdiet.common.core.domain.AjaxResult;
import com.stdiet.common.core.page.TableDataInfo;
import com.stdiet.custom.domain.SysCustomer;
import com.stdiet.custom.domain.SysCustomerHealthy;
import com.stdiet.custom.domain.SysPhysicalSigns;
import com.stdiet.custom.dto.request.CustomerInvestigateRequest;
import com.stdiet.custom.service.ISysCustomerHealthyService;
import com.stdiet.custom.service.ISysCustomerService;
import com.stdiet.custom.service.ISysPhysicalSignsService;
import com.stdiet.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private ISysCustomerService iSysCustomerService;

    @Autowired
    private ISysPhysicalSignsService iSysPhysicalSignsService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysCustomerHealthyService sysCustomerHealthyService;

    /**
     * 建立客户信息档案
     */
    @PostMapping("/customerInvestigate")
    public AjaxResult customerInvestigate(@RequestBody CustomerInvestigateRequest customerInvestigateRequest) throws Exception
    {
        //验证是否已存在该手机号
        SysCustomer phoneCustomer = iSysCustomerService.getCustomerByPhone(customerInvestigateRequest.getPhone());
        if(phoneCustomer != null){
            return AjaxResult.error("该手机号已存在");
        }
        customerInvestigateRequest.setId(null); //只能添加，无法修改
        return AjaxResult.success(iSysCustomerService.addOrupdateCustomerAndSign(customerInvestigateRequest));
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
     * 新增客户健康
     */
    @PostMapping("/addCustomerHealthy")
    public AjaxResult addCustomerHealthy(@RequestBody SysCustomerHealthy sysCustomerHealthy)
    {
        System.out.println(sysCustomerHealthy.getSex());
        return toAjax(1);
    }

}
