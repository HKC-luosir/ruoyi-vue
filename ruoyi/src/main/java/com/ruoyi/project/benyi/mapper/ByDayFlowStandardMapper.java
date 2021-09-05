package com.ruoyi.project.benyi.mapper;

import java.util.List;
import com.ruoyi.project.benyi.domain.ByDayFlowStandard;

/**
 * 一日流程标准Mapper接口
 * 
 * @author tsbz
 * @date 2020-05-18
 */
public interface ByDayFlowStandardMapper 
{
    /**
     * 查询一日流程标准
     * 
     * @param sid 一日流程标准ID
     * @return 一日流程标准
     */
    public ByDayFlowStandard selectByDayFlowStandardById(Long sid);

    /**
     * 查询一日流程标准列表
     * 
     * @param byDayFlowStandard 一日流程标准
     * @return 一日流程标准集合
     */
    public List<ByDayFlowStandard> selectByDayFlowStandardList(ByDayFlowStandard byDayFlowStandard);

    /**
     * 查询一日流程标准列表
     *
     * @param byDayFlowStandard 一日流程标准
     * @return 一日流程标准集合
     */
    public List<ByDayFlowStandard> selectByDayFlowStandardListAssessment(ByDayFlowStandard byDayFlowStandard);

    /**
     * 新增一日流程标准
     * 
     * @param byDayFlowStandard 一日流程标准
     * @return 结果
     */
    public int insertByDayFlowStandard(ByDayFlowStandard byDayFlowStandard);

    /**
     * 修改一日流程标准
     * 
     * @param byDayFlowStandard 一日流程标准
     * @return 结果
     */
    public int updateByDayFlowStandard(ByDayFlowStandard byDayFlowStandard);

    /**
     * 删除一日流程标准
     * 
     * @param sid 一日流程标准ID
     * @return 结果
     */
    public int deleteByDayFlowStandardById(Long sid);

    /**
     * 批量删除一日流程标准
     * 
     * @param sids 需要删除的数据ID
     * @return 结果
     */
    public int deleteByDayFlowStandardByIds(Long[] sids);
}