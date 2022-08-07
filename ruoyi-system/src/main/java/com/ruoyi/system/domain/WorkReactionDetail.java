package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 work_reaction_detail
 * 
 * @author ruoyi
 * @date 2022-08-05
 */
public class WorkReactionDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long reactionDetailId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String reactionCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long sort;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String processName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String processCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String precondition;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String param;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String endif;

    public void setReactionDetailId(Long reactionDetailId) 
    {
        this.reactionDetailId = reactionDetailId;
    }

    public Long getReactionDetailId() 
    {
        return reactionDetailId;
    }
    public void setReactionCode(String reactionCode) 
    {
        this.reactionCode = reactionCode;
    }

    public String getReactionCode() 
    {
        return reactionCode;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setProcessName(String processName) 
    {
        this.processName = processName;
    }

    public String getProcessName() 
    {
        return processName;
    }
    public void setProcessCode(String processCode) 
    {
        this.processCode = processCode;
    }

    public String getProcessCode() 
    {
        return processCode;
    }
    public void setPrecondition(String precondition) 
    {
        this.precondition = precondition;
    }

    public String getPrecondition() 
    {
        return precondition;
    }
    public void setParam(String param) 
    {
        this.param = param;
    }

    public String getParam() 
    {
        return param;
    }
    public void setEndif(String endif) 
    {
        this.endif = endif;
    }

    public String getEndif() 
    {
        return endif;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("reactionDetailId", getReactionDetailId())
            .append("reactionCode", getReactionCode())
            .append("sort", getSort())
            .append("processName", getProcessName())
            .append("processCode", getProcessCode())
            .append("precondition", getPrecondition())
            .append("param", getParam())
            .append("endif", getEndif())
            .toString();
    }
}