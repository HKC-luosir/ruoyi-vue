package com.ruoyi.project.benyi.service;

import java.util.List;
import com.ruoyi.project.benyi.domain.ByChildHealthCheck;

/**
 * 儿童常规体检记录Service接口
 *
 * @author tsbz
 * @date 2020-08-07
 */
public interface IByChildHealthCheckService
{
    /**
     * 查询儿童常规体检记录
     *
     * @param id 儿童常规体检记录ID
     * @return 儿童常规体检记录
     */
    public ByChildHealthCheck selectByChildHealthCheckById(Long id);

    /**
     * 查询儿童常规体检记录列表
     *
     * @param byChildHealthCheck 儿童常规体检记录
     * @return 儿童常规体检记录集合
     */
    public List<ByChildHealthCheck> selectByChildHealthCheckList(ByChildHealthCheck byChildHealthCheck);

    /**
     * 新增儿童常规体检记录
     *
     * @param byChildHealthCheck 儿童常规体检记录
     * @return 结果
     */
    public int insertByChildHealthCheck(ByChildHealthCheck byChildHealthCheck);

    /**
     * 修改儿童常规体检记录
     *
     * @param byChildHealthCheck 儿童常规体检记录
     * @return 结果
     */
    public int updateByChildHealthCheck(ByChildHealthCheck byChildHealthCheck);

    /**
     * 批量删除儿童常规体检记录
     *
     * @param ids 需要删除的儿童常规体检记录ID
     * @return 结果
     */
    public int deleteByChildHealthCheckByIds(Long[] ids);

    /**
     * 删除儿童常规体检记录信息
     *
     * @param id 儿童常规体检记录ID
     * @return 结果
     */
    public int deleteByChildHealthCheckById(Long id);
}