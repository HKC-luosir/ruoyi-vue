package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.ByClass;

/**
 * 班级信息Mapper接口
 *
 * @author tsbz
 * @date 2020-04-14
 */
public interface ByClassMapper
{
    /**
     * 查询班级信息
     *
     * @param bjbh 班级信息ID
     * @return 班级信息
     */
    public ByClass selectByClassById(String bjbh);

    /**
     * 查询班级信息列表
     *
     * @param byClass 班级信息
     * @return 班级信息集合
     */
    public List<ByClass> selectByClassList(ByClass byClass);

    /**
     * 查询班级信息列表
     *
     * @param byClass 班级信息
     * @return 班级信息集合
     */
    public List<ByClass> selectByClassListAll(ByClass byClass);

    //通过userID查询class
    public ByClass selectByClassByUserId(ByClass byClass);

    /**
     * 新增班级信息
     *
     * @param byClass 班级信息
     * @return 结果
     */
    public int insertByClass(ByClass byClass);

    /**
     * 修改班级信息
     *
     * @param byClass 班级信息
     * @return 结果
     */
    public int updateByClass(ByClass byClass);

    /**
     * 删除班级信息
     *
     * @param bjbh 班级信息ID
     * @return 结果
     */
    public int deleteByClassById(String bjbh);

    /**
     * 批量删除班级信息
     *
     * @param bjbhs 需要删除的数据ID
     * @return 结果
     */
    public int deleteByClassByIds(String[] bjbhs);

    /**
     * 查询班级信息列表
     *
     * @param byClass 班级信息
     * @return 班级信息集合
     */
    public List<ByClass> selectststicstSchoolList(ByClass byClass);
}