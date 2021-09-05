package com.ruoyi.project.benyi.mapper;

import java.util.List;

import com.ruoyi.project.benyi.domain.ByThemeWeekplan;

/**
 * 主题整合周计划（根据月计划明细）Mapper接口
 *
 * @author tsbz
 * @date 2020-08-26
 */
public interface ByThemeWeekplanMapper {
    /**
     * 查询主题整合周计划（根据月计划明细）
     *
     * @param id 主题整合周计划（根据月计划明细）ID
     * @return 主题整合周计划（根据月计划明细）
     */
    public ByThemeWeekplan selectByThemeWeekplanById(String id);

    /**
     * 查询主题整合周计划（根据月计划明细）列表
     *
     * @param byThemeWeekplan 主题整合周计划（根据月计划明细）
     * @return 主题整合周计划（根据月计划明细）集合
     */
    public List<ByThemeWeekplan> selectByThemeWeekplanList(ByThemeWeekplan byThemeWeekplan);

    /**
     * 新增主题整合周计划（根据月计划明细）
     *
     * @param byThemeWeekplan 主题整合周计划（根据月计划明细）
     * @return 结果
     */
    public int insertByThemeWeekplan(ByThemeWeekplan byThemeWeekplan);

    /**
     * 修改主题整合周计划（根据月计划明细）
     *
     * @param byThemeWeekplan 主题整合周计划（根据月计划明细）
     * @return 结果
     */
    public int updateByThemeWeekplan(ByThemeWeekplan byThemeWeekplan);

    /**
     * 删除主题整合周计划（根据月计划明细）
     *
     * @param id 主题整合周计划（根据月计划明细）ID
     * @return 结果
     */
    public int deleteByThemeWeekplanById(String id);

    /**
     * 批量删除主题整合周计划（根据月计划明细）
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteByThemeWeekplanByIds(String[] ids);
}