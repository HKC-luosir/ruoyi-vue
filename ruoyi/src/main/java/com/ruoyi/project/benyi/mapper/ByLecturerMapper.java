package com.ruoyi.project.benyi.mapper;

import java.util.List;
import com.ruoyi.project.benyi.domain.ByLecturer;

/**
 * 讲师Mapper接口
 * 
 * @author tsbz
 * @date 2020-05-25
 */
public interface ByLecturerMapper 
{
    /**
     * 查询讲师
     * 
     * @param id 讲师ID
     * @return 讲师
     */
    public ByLecturer selectByLecturerById(Long id);

    /**
     * 查询讲师列表
     * 
     * @param byLecturer 讲师
     * @return 讲师集合
     */
    public List<ByLecturer> selectByLecturerList(ByLecturer byLecturer);

    /**
     * 新增讲师
     * 
     * @param byLecturer 讲师
     * @return 结果
     */
    public int insertByLecturer(ByLecturer byLecturer);

    /**
     * 修改讲师
     * 
     * @param byLecturer 讲师
     * @return 结果
     */
    public int updateByLecturer(ByLecturer byLecturer);

    /**
     * 删除讲师
     * 
     * @param id 讲师ID
     * @return 结果
     */
    public int deleteByLecturerById(Long id);

    /**
     * 批量删除讲师
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteByLecturerByIds(Long[] ids);
}