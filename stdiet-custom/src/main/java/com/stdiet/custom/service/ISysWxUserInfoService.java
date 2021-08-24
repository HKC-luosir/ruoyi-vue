package com.stdiet.custom.service;

import java.util.List;
import com.stdiet.custom.domain.SysWxUserInfo;

/**
 * 微信用户Service接口
 *
 * @author wonder
 * @date 2020-11-28
 */
public interface ISysWxUserInfoService
{
    /**
     * 查询微信用户
     *
     * @param openid 微信用户ID
     * @return 微信用户
     */
    public SysWxUserInfo selectSysWxUserInfoById(String openid);

    public SysWxUserInfo selectSysWxUserInfoByCusId(Long cusId);

    /**
     * 查询微信用户列表
     *
     * @param sysWxUserInfo 微信用户
     * @return 微信用户集合
     */
    public List<SysWxUserInfo> selectSysWxUserInfoList(SysWxUserInfo sysWxUserInfo);

    public List<SysWxUserInfo> selectSysWxUserInfoListNot(SysWxUserInfo sysWxUserInfo);

    /**
     * 新增微信用户
     *
     * @param sysWxUserInfo 微信用户
     * @return 结果
     */
    public int insertSysWxUserInfo(SysWxUserInfo sysWxUserInfo);

    /**
     * 修改微信用户
     *
     * @param sysWxUserInfo 微信用户
     * @return 结果
     */
    public int updateSysWxUserInfo(SysWxUserInfo sysWxUserInfo);

    /**
     * 批量删除微信用户
     *
     * @param openids 需要删除的微信用户ID
     * @return 结果
     */
    public int deleteSysWxUserInfoByIds(String[] openids);

    /**
     * 删除微信用户信息
     *
     * @param openid 微信用户ID
     * @return 结果
     */
    public int deleteSysWxUserInfoById(String openid);

    /**
     * 根据手机号查询微信用户
     * @param phone
     * @return
     */
    public SysWxUserInfo getSysWxUserInfoByPhone(String phone);

    /**
     * 根据openid移除对应绑定的cusId
     * @param openid
     * @return
     */
    public int removeCusIdByOpenId(String openid);

    /**
     * 根据cusId更新健康减脂宣言
     * @param sysWxUserInfo
     * @return
     */
    public int updateHealthManifestoByCusId(SysWxUserInfo sysWxUserInfo);


}