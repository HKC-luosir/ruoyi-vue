package com.stdiet.custom.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stdiet.common.annotation.Excel;
import com.stdiet.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 每个订单的提成详情
 * */
@Data
public class SysOrderCommisionDayDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**订单ID*/
    @Excel(name = "订单ID")
    private Long orderId;

    /**订单成交时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "订单成交时间")
    private LocalDateTime orderTime;

    /**客户姓名*/
    @Excel(name = "客户姓名")
    private String name;

    /**订单金额或服务订单金额*/
    @Excel(name = "订单金额")
    private BigDecimal orderAmount;

    /**服务开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "服务开始时间")
    private LocalDate serverStartDate;

    /**服务结束时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "服务结束时间")
    private LocalDate serverEndDate;

    /**服务月数*/
    private Integer serverMonth;

    /**服务天数*/
    @Excel(name = "服务天数")
    private Integer serverDay;

    /**赠送天数*/
    private Integer giveDay;

    /** 暂停总天数 **/

    private int pauseTotalDay;

    //已发放提成
    private BigDecimal hasSendOrderCommission;

    //未发放提成
    private BigDecimal notHasSendOrderCommission;

    /**每天金额*/
    @Excel(name = "每天金额")
    private BigDecimal dayMoney;

    /**每年每月暂停天数*/
    private Map<String, Integer> everyYearMonthPauseDay;

    /**每年每月服务天数**/
    private Map<String, Integer> everyYearMonthServerDay;

    /**每年每月对应金额*/
    private Map<String, BigDecimal> everyYearMonthServerMoney;

    /**每年每月对应提成*/
    private Map<String, BigDecimal> everyYearMonthServerCommission;

    /**每年每月的提成是否发放**/
    private Map<String, Boolean> everyYearMonthCommissionSendFlag;

    //该笔订单成交的当月的总成交额，用于确定提成比例
    @Excel(name = "当月的成交额")
    private BigDecimal monthOrderTotalAmount;

    //该笔订单对应提成比例
    @Excel(name = "提成比例")
    private Float commissionRate;

    /**订单总提成**/
    @Excel(name = "订单提成")
    private BigDecimal orderCommission;

    //售后
    private Long afterSaleId;

    //营养师
    private Long nutritionistId;
}