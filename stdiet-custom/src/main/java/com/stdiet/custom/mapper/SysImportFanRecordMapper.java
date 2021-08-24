package com.stdiet.custom.mapper;

import java.util.List;
import java.util.Map;

import com.stdiet.custom.domain.SysImportFanRecord;
import com.stdiet.custom.domain.SysWxFanStatistics;

/**
 * 导粉管理Mapper接口
 *
 * @author xzj
 * @date 2021-05-17
 */
public interface SysImportFanRecordMapper
{
    /**
     * 查询导粉管理
     *
     * @param id 导粉管理ID
     * @return 导粉管理
     */
    public SysImportFanRecord selectSysImportFanRecordById(Long id);

    /**
     * 查询导粉管理列表
     *
     * @param sysImportFanRecord 导粉管理
     * @return 导粉管理集合
     */
    public List<SysImportFanRecord> selectSysImportFanRecordList(SysImportFanRecord sysImportFanRecord);

    /**
     * 新增导粉管理
     *
     * @param sysImportFanRecord 导粉管理
     * @return 结果
     */
    public int insertSysImportFanRecord(SysImportFanRecord sysImportFanRecord);

    /**
     * 修改导粉管理
     *
     * @param sysImportFanRecord 导粉管理
     * @return 结果
     */
    public int updateSysImportFanRecord(SysImportFanRecord sysImportFanRecord);

    /**
     * 删除导粉管理
     *
     * @param id 导粉管理ID
     * @return 结果
     */
    public int deleteSysImportFanRecordById(Long id);

    /**
     * 批量删除导粉管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysImportFanRecordByIds(Long[] ids);

    /**
     * 根据日期渠道、直播间获取进粉记录
     * @param sysImportFanRecord
     * @return
     */
    public SysImportFanRecord getFanRecordByChannelLive(SysImportFanRecord sysImportFanRecord);

    //查询总导粉数量
    int selectTotalSysImportFanNum(SysImportFanRecord sysImportFanRecord);

    /**
     * 根据时间范围统计各个渠道的总导粉数量
     * @param sysWxFanStatistics
     * @return
     */
    List<Map<String,Object>> getTotalFanNumGroupByChannel(SysWxFanStatistics sysWxFanStatistics);
}