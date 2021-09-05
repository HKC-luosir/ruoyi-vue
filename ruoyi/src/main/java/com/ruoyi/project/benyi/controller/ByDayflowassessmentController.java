package com.ruoyi.project.benyi.controller;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.benyi.domain.ByDayFlowStandard;
import com.ruoyi.project.benyi.domain.ByDayflowassessmentitem;
import com.ruoyi.project.benyi.domain.ByDayflowassessmentplan;
import com.ruoyi.project.benyi.service.IByDayFlowStandardService;
import com.ruoyi.project.benyi.service.IByDayflowassessmentitemService;
import com.ruoyi.project.benyi.service.IByDayflowassessmentplanService;
import com.ruoyi.project.common.SchoolCommon;
import com.ruoyi.project.system.domain.ByClass;
import com.ruoyi.project.system.service.IByClassService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.benyi.domain.ByDayflowassessment;
import com.ruoyi.project.benyi.service.IByDayflowassessmentService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 幼儿园一日流程评估Controller
 *
 * @author tsbz
 * @date 2021-02-05
 */
@RestController
@RequestMapping("/benyi/dayflowassessment")
public class ByDayflowassessmentController extends BaseController {
    @Autowired
    private IByDayflowassessmentService byDayflowassessmentService;
    @Autowired
    private IByClassService byClassService;
    @Autowired
    private SchoolCommon schoolCommon;
    @Autowired
    private IByDayflowassessmentitemService byDayflowassessmentitemService;

    /**
     * 查询幼儿园一日流程评估列表
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:list')")
    @GetMapping("/list")
    public TableDataInfo list(ByDayflowassessment byDayflowassessment) {
//        byDayflowassessment.setCreateUserid(SecurityUtils.getLoginUser().getUser().getUserId());
        startPage();
        List<ByDayflowassessment> list = byDayflowassessmentService.selectByDayflowassessmentList(byDayflowassessment);
        return getDataTable(list);
    }

    /**
     * 查询幼儿园一日流程评估列表
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:list')")
    @GetMapping("/listpjf")
    public TableDataInfo listpjf(ByDayflowassessment byDayflowassessment) {
        byDayflowassessment.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        startPage();
        List<ByDayflowassessment> list = byDayflowassessmentService.selectByDayflowassessmentPjf(byDayflowassessment);
        return getDataTable(list);
    }

    /**
     * 查询幼儿园一日流程评估列表
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:list')")
    @GetMapping("/listteacheravg")
    public TableDataInfo listteacheravg(ByDayflowassessment byDayflowassessment) {
        byDayflowassessment.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        startPage();
        List<ByDayflowassessment> list = byDayflowassessmentService.selectByDayflowassessmentTeacherPjf(byDayflowassessment);
        return getDataTable(list);
    }

    /**
     * 查询幼儿园一日流程评估列表
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:list')")
    @GetMapping("/listmyself")
    public TableDataInfo listmyself(ByDayflowassessment byDayflowassessment) {
        byDayflowassessment.setPgdx(SecurityUtils.getLoginUser().getUser().getUserId());
        startPage();
        List<ByDayflowassessment> list = byDayflowassessmentService.selectByDayflowassessmentList(byDayflowassessment);
        return getDataTable(list);
    }

    /**
     * 查询幼儿园一日流程评估列表
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:list')")
    @GetMapping("/listbyjsid")
    public TableDataInfo listbyjsid(ByDayflowassessment byDayflowassessment) {
        System.out.println(byDayflowassessment.getPgdx());
        List<ByDayflowassessment> list = byDayflowassessmentService.selectByDayflowassessmentList(byDayflowassessment);
        return getDataTable(list);
    }

    /**
     * 导出幼儿园一日流程评估列表
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:export')")
    @Log(title = "幼儿园一日流程评估", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ByDayflowassessment byDayflowassessment) {
        List<ByDayflowassessment> list = byDayflowassessmentService.selectByDayflowassessmentList(byDayflowassessment);
        ExcelUtil<ByDayflowassessment> util = new ExcelUtil<ByDayflowassessment>(ByDayflowassessment.class);
        return util.exportExcel(list, "dayflowassessment");
    }

    /**
     * 获取幼儿园一日流程评估详细信息
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(byDayflowassessmentService.selectByDayflowassessmentById(id));
    }

    /**
     * 新增幼儿园一日流程评估
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:add')")
    @Log(title = "幼儿园一日流程评估", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ByDayflowassessment byDayflowassessment) {
        //首先判断byDayflowassessment.id 是否为空
        if (byDayflowassessment.getId() == null) {
            //判断当前评估对象的角色是主班 配班 还是助理教师
            Long pgdx = byDayflowassessment.getPgdx();
            //获取班级信息
            String classId = byDayflowassessment.getClassid();
            ByClass byClass = byClassService.selectByClassById(classId);
            if (byClass != null) {
                Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
                Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
                String xnXq = schoolCommon.getCurrentXnXq();
                byDayflowassessment.setDeptId(deptId);
                byDayflowassessment.setCreateUserid(userId);
                byDayflowassessment.setXnxq(xnXq);
                //获取总得分
                Double dTotal = GetDf(byDayflowassessment.getList());
                byDayflowassessment.setZzdf(dTotal);
                if (byClass.getZbjs() == null) {
                    return AjaxResult.error("未设置班级主班教师，请学校管理员设置班级信息");
                }
                System.out.println(byClass.getZbjs().longValue() == pgdx.longValue());
                //如果评估对象非主班教师，那么对主班教师产生相同的扣分项
                if (byClass.getZbjs().longValue() == pgdx.longValue()) {
                    int iRows = addDayFlowAssessment(byDayflowassessment);
                    return toAjax(iRows);
                } else {
                    //评估对象为助理教师和配班教师
                    int iRows = addDayFlowAssessment(byDayflowassessment);
                    ByDayflowassessment byDayflowassessmentNew = new ByDayflowassessment();
                    byDayflowassessmentNew.setClassid(classId);
                    byDayflowassessmentNew.setDeptId(deptId);
                    byDayflowassessmentNew.setCreateUserid(userId);
                    byDayflowassessmentNew.setXnxq(xnXq);
                    //获取总得分
                    byDayflowassessmentNew.setZzdf(dTotal);

                    if (byClass.getZbjs() == null) {
                        System.out.println("未设置主班教师");
                    } else {
                        byDayflowassessmentNew.setPgdx(byClass.getZbjs());//设置评估对象为主班教师
                        byDayflowassessmentNew.setPgdxxm(byClass.getZbjsxm());
                        byDayflowassessmentNew.setStatus("1");//永远是提交状态，因为是助理或配班教师所产生的数据项
                        byDayflowassessmentNew.setRemark(byDayflowassessment.getId().toString());

                        iRows = iRows + addDayFlowAssessment(byDayflowassessmentNew);
                    }
                    return toAjax(iRows);
                }
            } else {
                return AjaxResult.error("班级信息错误");
            }
        } else {
            //id 不为空，说明是修改
            ByDayflowassessment byDayflowassessmentModel = byDayflowassessmentService.selectByDayflowassessmentById(byDayflowassessment.getId());
            Double dTotal = GetDf(byDayflowassessment.getList());
            byDayflowassessmentModel.setZzdf(dTotal);
            byDayflowassessmentModel.setList(byDayflowassessment.getList());
            byDayflowassessmentModel.setStatus(byDayflowassessment.getStatus());
            //判断当前评估对象的角色是主班 配班 还是助理教师
            Long pgdx = byDayflowassessmentModel.getPgdx();
            //获取班级信息
            String classId = byDayflowassessmentModel.getClassid();
            ByClass byClass = byClassService.selectByClassById(classId);
            if (byClass.getZbjs().longValue() == pgdx.longValue()) {
                //首先清除item
                byDayflowassessmentitemService.deleteByDayflowassessmentitemByPid(byDayflowassessmentModel.getId());
                int iRows = addDayFlowAssessment(byDayflowassessmentModel);
                return toAjax(iRows);
            } else {
                //清空item
                byDayflowassessmentitemService.deleteByDayflowassessmentitemByPid(byDayflowassessmentModel.getId());
                //评估对象为助理教师和配班教师
                int iRows = addDayFlowAssessment(byDayflowassessmentModel);
                ByDayflowassessment byDayflowassessmentNew = new ByDayflowassessment();
                byDayflowassessmentNew.setZzdf(dTotal);
                byDayflowassessmentNew.setList(byDayflowassessment.getList());
                if (byClass.getZbjs() == null) {
                    System.out.println("未设置主班教师");
                } else {
                    //主班教师被评估记录的id
                    ByDayflowassessment byDayflowassessmentZbjs = new ByDayflowassessment();
                    byDayflowassessmentZbjs.setRemark(byDayflowassessmentModel.getId().toString());
                    Long id = byDayflowassessmentService.selectByDayflowassessmentList(byDayflowassessmentZbjs).get(0).getId();
                    //清空item
                    byDayflowassessmentitemService.deleteByDayflowassessmentitemByPid(id);
                    byDayflowassessmentNew.setId(id);
                    byDayflowassessmentNew.setStatus("1");//永远是提交状态，因为是助理或配班教师所产生的数据项
                    byDayflowassessmentNew.setPgdx(byClass.getZbjs());//设置评估对象为主班教师
                    byDayflowassessmentNew.setPgdxxm(byClass.getZbjsxm());

                    iRows = iRows + addDayFlowAssessment(byDayflowassessmentNew);
                }
                return toAjax(iRows);
            }
        }
    }

    //
    public Integer addDayFlowAssessment(ByDayflowassessment byDayflowassessment) {
        int iRows = 0;
        if (byDayflowassessment.getId() == null) {
            iRows = byDayflowassessmentService.insertByDayflowassessment(byDayflowassessment);
        } else {
            iRows = byDayflowassessmentService.updateByDayflowassessment(byDayflowassessment);
        }

        List<ByDayFlowStandard> list = byDayflowassessment.getList();

        if (list != null && list.size() > 0) {
            //将传进来的评分值存入item表
            ByDayflowassessmentitem byDayflowassessmentitem = null;
            for (int i = 0; i < list.size(); i++) {
                byDayflowassessmentitem = new ByDayflowassessmentitem();
                String mrz = list.get(i).getMrz();
                if (mrz != "0" && !schoolCommon.isStringEmpty(mrz)) {
                    Double dMrz = Double.valueOf(mrz);
                    if (dMrz != 0) {
                        byDayflowassessmentitem.setPid(byDayflowassessment.getId());
                        byDayflowassessmentitem.setItem(list.get(i).getId());
                        byDayflowassessmentitem.setValue(dMrz);
                        iRows = iRows + byDayflowassessmentitemService.insertByDayflowassessmentitem(byDayflowassessmentitem);
                    }
                }
            }
        }
        return iRows;
    }

    public Double GetDf(List<ByDayFlowStandard> list) {
        Double df = (double) 0;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String mrz = list.get(i).getMrz();
                if (mrz != "0" && !schoolCommon.isStringEmpty(mrz)) {
                    Double dMrz = Double.valueOf(mrz);
                    if (dMrz != 0) {
                        df = df + dMrz;
                    }
                }
            }
        }
        return df;
    }

    /**
     * 修改幼儿园一日流程评估
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:edit')")
    @Log(title = "幼儿园一日流程评估", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ByDayflowassessment byDayflowassessment) {
        AjaxResult ajax = AjaxResult.success();
        return ajax;
    }

    /**
     * 删除幼儿园一日流程评估
     */
    @PreAuthorize("@ss.hasPermi('benyi:dayflowassessment:remove')")
    @Log(title = "幼儿园一日流程评估", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        //判断当前评估是不是配班和助理教师评估，如果是的话，应该删除对应的主班教师评估
        for (int i = 0; i < ids.length; i++) {
            ByDayflowassessment byDayflowassessment = new ByDayflowassessment();
            byDayflowassessment.setRemark(ids[i].toString());
            List<ByDayflowassessment> list = byDayflowassessmentService.selectByDayflowassessmentList(byDayflowassessment);
            if (list != null && list.size() > 0) {
                Long newId=list.get(0).getId();
                //删除item数据
                byDayflowassessmentitemService.deleteByDayflowassessmentitemByPid(newId);
                //删除主数据
                byDayflowassessmentService.deleteByDayflowassessmentById(newId);
            }
        }
        //先删除item数据
        for (int i = 0; i < ids.length; i++) {
            byDayflowassessmentitemService.deleteByDayflowassessmentitemByPid(ids[i]);
        }
        return toAjax(byDayflowassessmentService.deleteByDayflowassessmentByIds(ids));
    }
}