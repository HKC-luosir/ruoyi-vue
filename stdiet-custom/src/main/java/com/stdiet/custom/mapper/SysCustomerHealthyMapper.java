package com.stdiet.custom.mapper;

import java.util.List;
import com.stdiet.custom.domain.SysCustomerHealthy;

/**
 * 客户健康Mapper接口
 *
 * @author xzj
 * @date 2021-01-23
 */
public interface SysCustomerHealthyMapper
{
    /**
     * 查询客户健康
     *
     * @param id 客户健康ID
     * @return 客户健康
     */
    public SysCustomerHealthy selectSysCustomerHealthyById(Long id);

    /**
     * 查询客户健康列表
     *
     * @param sysCustomerHealthy 客户健康
     * @return 客户健康集合
     */
    public List<SysCustomerHealthy> selectSysCustomerHealthyList(SysCustomerHealthy sysCustomerHealthy);

    /**
     * 新增客户健康
     *
     * @param sysCustomerHealthy 客户健康
     * @return 结果
     */
    public int insertSysCustomerHealthy(SysCustomerHealthy sysCustomerHealthy);

    /**
     * 修改客户健康
     *
     * @param sysCustomerHealthy 客户健康
     * @return 结果
     */
    public int updateSysCustomerHealthy(SysCustomerHealthy sysCustomerHealthy);

    /**
     * 删除客户健康
     *
     * @param id 客户健康ID
     * @return 结果
     */
    public int deleteSysCustomerHealthyById(Long id);

    /**
     * 批量删除客户健康
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysCustomerHealthyByIds(Long[] ids);
}