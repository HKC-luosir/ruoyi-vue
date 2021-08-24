package com.stdiet.web.controller.custom;

import java.util.List;

import com.stdiet.common.utils.StringUtils;
import com.stdiet.custom.domain.SysCustomerPhysicalSigns;
import com.stdiet.custom.domain.SysPhysicalSigns;
import com.stdiet.custom.dto.request.HealthyDetailRequest;
import com.stdiet.custom.service.ISysCustomerPhysicalSignsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.stdiet.common.annotation.Log;
import com.stdiet.common.core.controller.BaseController;
import com.stdiet.common.core.domain.AjaxResult;
import com.stdiet.common.enums.BusinessType;
import com.stdiet.custom.domain.SysCustomerHealthy;
import com.stdiet.custom.service.ISysCustomerHealthyService;
import com.stdiet.common.utils.poi.ExcelUtil;
import com.stdiet.common.core.page.TableDataInfo;

/**
 * 客户健康Controller
 *
 * @author xzj
 * @date 2021-01-23
 */
@RestController
@RequestMapping("/custom/healthy")
public class SysCustomerHealthyController extends BaseController
{
    @Autowired
    private ISysCustomerHealthyService sysCustomerHealthyService;

    @Autowired
    private ISysCustomerPhysicalSignsService sysCustomerPhysicalSignsService;

    /**
     * 查询客户健康列表
     */
    @PreAuthorize("@ss.hasPermi('custom:healthy:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCustomerHealthy sysCustomerHealthy)
    {
        startPage();
        List<SysCustomerHealthy> list = sysCustomerHealthyService.selectSysCustomerHealthyList(sysCustomerHealthy);
        for(SysCustomerHealthy customerHealthy : list){
            if(StringUtils.isNotEmpty(customerHealthy.getPhone())){
                customerHealthy.setPhone(StringUtils.hiddenPhoneNumber(customerHealthy.getPhone()));
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出客户健康列表
     */
    @PreAuthorize("@ss.hasPermi('custom:healthy:export')")
    @Log(title = "客户健康", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCustomerHealthy sysCustomerHealthy)
    {
        List<SysCustomerHealthy> list = sysCustomerHealthyService.selectSysCustomerHealthyList(sysCustomerHealthy);
        for(SysCustomerHealthy customerHealthy : list){
            if(StringUtils.isNotEmpty(customerHealthy.getPhone())){
                customerHealthy.setPhone(StringUtils.hiddenPhoneNumber(customerHealthy.getPhone()));
            }
        }
        ExcelUtil<SysCustomerHealthy> util = new ExcelUtil<SysCustomerHealthy>(SysCustomerHealthy.class);
        return util.exportExcel(list, "healthy");
    }

    /**
     * 获取客户健康详细信息
     */
    @PreAuthorize("@ss.hasPermi('custom:healthy:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        SysCustomerHealthy sysCustomerHealthy = sysCustomerHealthyService.selectSysCustomerHealthyById(id);
        if(sysCustomerHealthy != null && StringUtils.isNotEmpty(sysCustomerHealthy.getPhone())){
            sysCustomerHealthy.setPhone(StringUtils.hiddenPhoneNumber(sysCustomerHealthy.getPhone()));
        }
        return AjaxResult.success(sysCustomerHealthy);
    }

    /**
     * 新增客户健康
     */
    @PreAuthorize("@ss.hasPermi('customer:healthy:add')")
    @Log(title = "客户健康", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCustomerHealthy sysCustomerHealthy)
    {
        return sysCustomerHealthyService.insertSysCustomerHealthy(sysCustomerHealthy);
    }

    /**
     * 修改客户健康
     */
    @PreAuthorize("@ss.hasPermi('custom:healthy:edit')")
    @Log(title = "客户健康", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCustomerHealthy sysCustomerHealthy)
    {
        return toAjax(sysCustomerHealthyService.updateSysCustomerHealthy(sysCustomerHealthy));
    }

    /**
     * 删除客户健康
     */
    @PreAuthorize("@ss.hasPermi('custom:healthy:remove')")
    @Log(title = "客户健康", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysCustomerHealthyService.deleteSysCustomerHealthyByIds(ids));
    }

    /**
     * 生成健康体征报告
     */
    @Log(title = "健康体征报告", businessType = BusinessType.INSERT)
    @PostMapping("/generateHealthyReport")
    public AjaxResult generateHealthyReport(@RequestBody  HealthyDetailRequest healthyDetailRequest)
    {
        return sysCustomerHealthyService.generateHealthyReport(healthyDetailRequest);
    }

    /**
     * 修改老的体征数据
     * @param sysPhysicalSigns
     * @return
     */
    @PutMapping("/edit/physical")
    public AjaxResult editPhysical(@RequestBody SysCustomerPhysicalSigns sysPhysicalSigns) {
        return toAjax(sysCustomerPhysicalSignsService.updateSysCustomerPhysicalSigns(sysPhysicalSigns));
    }
}