package com.stdiet.web.controller.custom;

import com.alibaba.fastjson.JSONArray;
import com.stdiet.common.annotation.Log;
import com.stdiet.common.core.domain.AjaxResult;
import com.stdiet.common.core.domain.entity.SysUser;
import com.stdiet.common.core.domain.model.LoginUser;
import com.stdiet.common.enums.BusinessType;
import com.stdiet.common.utils.SecurityUtils;
import com.stdiet.common.utils.StringUtils;
import com.stdiet.common.utils.poi.ExcelUtil;
import com.stdiet.custom.controller.OrderBaseController;
import com.stdiet.custom.domain.SysOrder;
import com.stdiet.custom.page.OrderTableDataInfo;
import com.stdiet.custom.service.ISysOrderService;
import com.stdiet.custom.utils.OrderUtils;
import com.stdiet.system.service.ISysDictDataService;
import com.stdiet.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单Controller
 *
 * @author wonder
 * @date 2020-09-24
 */
@RestController
@RequestMapping("/custom/order")
public class SysOrderController extends OrderBaseController {
    @Autowired
    private ISysOrderService sysOrderService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('custom:order:list')")
    @GetMapping("/list")
    public OrderTableDataInfo list(SysOrder sysOrder) {
        startPage();
        dealOrderType(sysOrder);
        List<SysOrder> list = new ArrayList<>();
        if (SecurityUtils.getLoginUser().getUser().getRoles().get(0).getRoleKey().equals("partner")) {
            String remark = SecurityUtils.getLoginUser().getUser().getRemark();
            if (StringUtils.isEmpty(remark)) {
                return getOrderDataTable(list, new BigDecimal(0));
            } else if (remark.contains("|") && StringUtils.isEmpty(sysOrder.getAccount())) {
                sysOrder.setAccRange(remark.split("\\|"));
            }
        }
        //限制订单权限，暂时先不放开
        /*LoginUser loginUser = SecurityUtils.getLoginUser();
        if(!SecurityUtils.isManager(loginUser)){
            System.out.println(loginUser.getUser().getUserId() + "-------------------------");
            sysOrder.setLoginUserId(loginUser.getUser().getUserId());
        }*/
        list = sysOrderService.selectSysOrderList(sysOrder);
        List<SysUser> userList = userService.selectAllUser();
        BigDecimal totalAmount = sysOrderService.selectAllOrderAmount(sysOrder);
        if (totalAmount == null) {
            totalAmount = new BigDecimal(0);
        }
        for (SysOrder order : list) {
            initUserNickNameAndOrderType(userList, order);
            if (StringUtils.isNotEmpty(order.getPhone())) {
                order.setPhone(StringUtils.hiddenPhoneNumber(order.getPhone()));
            }
        }
        return getOrderDataTable(list, totalAmount);
    }

    /**
     * 处理订单类型
     *
     * @param sysOrder
     */
    private void dealOrderType(SysOrder sysOrder) {
        if (StringUtils.isNotEmpty(sysOrder.getOrderType())) {
            try {
                JSONArray array = JSONArray.parseArray(URLDecoder.decode(sysOrder.getOrderType(), "UTF-8"));
                sysOrder.setSearchOrderTypeArray(array.size() > 0 ? array : null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('custom:order:export')")
    @Log(title = "销售订单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysOrder sysOrder) {
        List<SysOrder> list = sysOrderService.selectSysOrderList(sysOrder);
        List<SysUser> userList = userService.selectAllUser();
        for (SysOrder order : list) {
            initUserNickNameAndOrderType(userList, order);
            if (StringUtils.isNotEmpty(order.getPhone())) {
                order.setPhone(StringUtils.hiddenPhoneNumber(order.getPhone()));
            }
        }
        ExcelUtil<SysOrder> util = new ExcelUtil<SysOrder>(SysOrder.class);
        return util.exportExcel(list, "order");
    }

    /**
     * 获取销售订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('custom:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId) {
        return AjaxResult.success(sysOrderService.selectSysOrderById(orderId));
    }

    /**
     * 获取销售订单详细信息(列出所有营养师、助理等名称)
     */
    @PreAuthorize("@ss.hasPermi('custom:order:query')")
    @GetMapping(value = "/getInfoDetail")
    public AjaxResult getInfoDetail(@RequestParam("orderId") Long orderId) {
        SysOrder order = sysOrderService.selectSysOrderById(orderId);
        if (order != null) {
            List<SysUser> userList = userService.selectAllUser();
            initUserNickNameAndOrderType(userList, order);
        }
        return AjaxResult.success(order);
    }

    private void initUserNickNameAndOrderType(List<SysUser> userList, SysOrder order) {
        for (SysUser user : userList) {
            if (user.getUserId().equals(order.getPreSaleId())) {
                order.setPreSale(user.getNickName());
            }
            if (user.getUserId().equals(order.getOnSaleId())) {
                order.setOnSale(user.getNickName());
            }
            if (user.getUserId().equals(order.getAfterSaleId())) {
                order.setAfterSale(user.getNickName());
            }
            if (user.getUserId().equals(order.getNutritionistId())) {
                order.setNutritionist(user.getNickName());
            }
            if (user.getUserId().equals(order.getNutriAssisId())) {
                order.setNutriAssis(user.getNickName());
            }
            if (user.getUserId().equals(order.getOperatorId())) {
                order.setOperator(user.getNickName());
            }
            if (user.getUserId().equals(order.getPlannerId())) {
                order.setPlanner(user.getNickName());
            }
            if (user.getUserId().equals(order.getPlannerAssisId())) {
                order.setPlannerAssis(user.getNickName());
            }
            if (user.getUserId().equals(order.getOperatorAssisId())) {
                order.setOperatorAssis(user.getNickName());
            }
            if (user.getUserId().equals(order.getPushPreSaleId())) {
                order.setPushPreSale(user.getNickName());
            }
            if (user.getUserId().equals(order.getBusinessAffairId())) {
                order.setBusinessAffair(user.getNickName());
            }
        }
        order.setOrderTypeName(OrderUtils.getOrderTypeName(order));
    }

    /**
     * 新增销售订单
     */
    @PreAuthorize("@ss.hasPermi('custom:order:add')")
    @Log(title = "销售订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysOrder sysOrder) {
        return toAjax(sysOrderService.insertSysOrder(sysOrder));
    }

    /**
     * 修改销售订单
     */
    @PreAuthorize("@ss.hasPermi('custom:order:edit')")
    @Log(title = "销售订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysOrder sysOrder) {
        return toAjax(sysOrderService.updateSysOrder(sysOrder));
    }

    /**
     * 删除销售订单
     */
    @PreAuthorize("@ss.hasPermi('custom:order:remove')")
    @Log(title = "销售订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderId}")
    public AjaxResult remove(@PathVariable Long orderId) {
        return toAjax(sysOrderService.deleteSysOrderById(orderId));
    }
}