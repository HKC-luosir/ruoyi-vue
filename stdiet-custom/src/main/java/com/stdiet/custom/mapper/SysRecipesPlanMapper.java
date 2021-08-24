package com.stdiet.custom.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.stdiet.custom.domain.SysRecipesPlan;
import com.stdiet.custom.domain.SysRecipesPlanListInfo;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;

/**
 * 食谱计划Mapper接口
 *
 * @author xzj
 * @date 2021-01-15
 */
public interface SysRecipesPlanMapper
{
    /**
     * 查询食谱计划
     *
     * @param id 食谱计划ID
     * @return 食谱计划
     */
    public SysRecipesPlan selectSysRecipesPlanById(Long id);

    /**
     * 查询食谱计划列表
     *
     * @param sysRecipesPlan 食谱计划
     * @return 食谱计划集合
     */
    public List<SysRecipesPlan> selectSysRecipesPlanList(SysRecipesPlan sysRecipesPlan);

    /**
     * 新增食谱计划
     *
     * @param sysRecipesPlan 食谱计划
     * @return 结果
     */
    public int insertSysRecipesPlan(SysRecipesPlan sysRecipesPlan);

    /**
     * 修改食谱计划
     *
     * @param sysRecipesPlan 食谱计划
     * @return 结果
     */
    public int updateSysRecipesPlan(SysRecipesPlan sysRecipesPlan);

    /**
     * 删除食谱计划
     *
     * @param id 食谱计划ID
     * @return 结果
     */
    public int deleteSysRecipesPlanById(Long id);

    /**
     * 批量删除食谱计划
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysRecipesPlanByIds(Long[] ids);

    /**
     * 批量添加食谱计划
     *
     * @return 结果
     */
    int insertBatch(List<SysRecipesPlan> sysRecipesPlanList);

    /**
     * 根据订单ID删除对应食谱计划
     * @param orderIds
     * @return
     */
    int delRecipesPlanByOrderId(Long[] orderIds);

    /**
     * 多条件查询食谱计划列表
     * @param sysRecipesPlan
     * @return
     */
    List<SysRecipesPlan> selectPlanListByCondition(SysRecipesPlan sysRecipesPlan);

    /**
     * 根据客户ID查询最后一天食谱计划
     * @param customerId
     * @return
     */
    SysRecipesPlan getLastDayRecipesPlan(@Param("customerId")Long customerId, @Param("orderTime") Date orderTime);

    /**
     * 根据订单ID查询食谱计划
     * @param sysRecipesPlan
     * @return
     */
    List<SysRecipesPlan> selectPlanListByOrderId(SysRecipesPlan sysRecipesPlan);

    Long getCusIdByOutId(String outId);

    List<SysRecipesPlanListInfo> selectRecipesPlanListInfo(String outId);

    List<SysRecipesPlanListInfo> selectRecipesPlanListInfoByCusId(Long cusId);


    List<SysRecipesPlan> selectPlanListByCusId(Long cusId);
    
    List<SysRecipesPlan> selectRecipesModelList(SysRecipesPlan sysRecipesPlan);


    /**
     * 批量更新食谱计划的开始时间、结束时间
     * @param list
     * @return
     */
    int updateMuchRecipesPlanDate(SysRecipesPlan sysRecipesPlan);

    /**
     * 查询食谱计划中对应食谱存在天数缺失的食谱计划
     * @param cusId
     * @return
     */
    List<Map<String,Object>> getNeedSupplyRecipesByCusId(@Param("cusId")Long cusId);
}