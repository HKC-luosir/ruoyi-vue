package com.stdiet.custom.mapper;

import java.util.List;
import com.stdiet.custom.domain.SysLiveSchedul;

/**
 * 直播排班Mapper接口
 *
 * @author xzj
 * @date 2021-05-12
 */
public interface SysLiveSchedulMapper
{
    /**
     * 查询直播排班
     *
     * @param id 直播排班ID
     * @return 直播排班
     */
    public SysLiveSchedul selectSysLiveSchedulById(Long id);

    /**
     * 查询直播排班列表
     *
     * @param sysLiveSchedul 直播排班
     * @return 直播排班集合
     */
    public List<SysLiveSchedul> selectSysLiveSchedulList(SysLiveSchedul sysLiveSchedul);

    /**
     * 新增直播排班
     *
     * @param sysLiveSchedul 直播排班
     * @return 结果
     */
    public int insertSysLiveSchedul(SysLiveSchedul sysLiveSchedul);

    /**
     * 修改直播排班
     *
     * @param sysLiveSchedul 直播排班
     * @return 结果
     */
    public int updateSysLiveSchedul(SysLiveSchedul sysLiveSchedul);

    /**
     * 删除直播排班
     *
     * @param id 直播排班ID
     * @return 结果
     */
    public int deleteSysLiveSchedulById(Long id);

    /**
     * 批量删除直播排班
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysLiveSchedulByIds(Long[] ids);

    /**
     * 根据直播时间查询是否重叠
     */
    public SysLiveSchedul getLiveSchedulByLiveTime(SysLiveSchedul sysLiveSchedul);

    /**
     * 更新部分字段
     * @param sysLiveSchedul
     * @return
     */
    public int updateSysLiveSchedulById(SysLiveSchedul sysLiveSchedul);

    /**
     * 根据ID查询上一条记录
     * @param sysLiveSchedul
     * @return
     */
    public SysLiveSchedul getLastLiveSchedulById(SysLiveSchedul sysLiveSchedul);

    /**
     * 获取所有计划
     * @param sysLiveSchedul
     * @return
     */
    public List<SysLiveSchedul> getAllLiveSchedulByDate(SysLiveSchedul sysLiveSchedul);

    /**
     * 获取最后一次直播计划
     * @return
     */
    public SysLiveSchedul getLastLiveSchedul();

    /**
     * 根据时间确定最近的直播记录
     * @return
     */
    public SysLiveSchedul getLiveSchedulByTime(SysLiveSchedul sysLiveSchedul);
}