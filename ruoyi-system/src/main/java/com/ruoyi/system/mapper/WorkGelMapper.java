package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WorkGel;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2022-08-05
 */
public interface WorkGelMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param gelId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public WorkGel selectWorkGelByGelId(Long gelId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param workGel 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<WorkGel> selectWorkGelList(WorkGel workGel);

    /**
     * 新增【请填写功能名称】
     * 
     * @param workGel 【请填写功能名称】
     * @return 结果
     */
    public int insertWorkGel(WorkGel workGel);

    /**
     * 修改【请填写功能名称】
     * 
     * @param workGel 【请填写功能名称】
     * @return 结果
     */
    public int updateWorkGel(WorkGel workGel);

    /**
     * 删除【请填写功能名称】
     * 
     * @param gelId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteWorkGelByGelId(Long gelId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param gelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkGelByGelIds(Long[] gelIds);
}