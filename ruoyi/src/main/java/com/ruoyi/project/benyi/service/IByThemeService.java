package com.ruoyi.project.benyi.service;

import java.util.List;

import com.ruoyi.framework.web.domain.TreeSelect;
import com.ruoyi.project.benyi.domain.ByTheme;

/**
 * 主题整合Service接口
 *
 * @author tsbz
 * @date 2020-07-01
 */
public interface IByThemeService {
    /**
     * 查询主题整合
     *
     * @param id 主题整合ID
     * @return 主题整合
     */
    public ByTheme selectByThemeById(Long id);

    /**
     * 查询主题整合列表
     *
     * @param byTheme 主题整合
     * @return 主题整合集合
     */
    public List<ByTheme> selectByThemeList(ByTheme byTheme);

    /**
     * 查询一日流程列表树
     *
     * @param byTheme 一日流程
     * @return 一日流程树集合
     */
    public List<ByTheme> selectByThemeListTree(ByTheme byTheme);

    /**
     * 构建前端所需要树结构
     *
     * @param byThemes 部门列表
     * @return 树结构列表
     */
    public List<ByTheme> buildThemeDetailTree(List<ByTheme> byThemes);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param byThemes 部门列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildThemeTreeSelect(List<ByTheme> byThemes);

    /**
     * 新增主题整合
     *
     * @param byTheme 主题整合
     * @return 结果
     */
    public int insertByTheme(ByTheme byTheme);

    /**
     * 修改主题整合
     *
     * @param byTheme 主题整合
     * @return 结果
     */
    public int updateByTheme(ByTheme byTheme);

    /**
     * 批量删除主题整合
     *
     * @param ids 需要删除的主题整合ID
     * @return 结果
     */
    public int deleteByThemeByIds(Long[] ids);

    /**
     * 删除主题整合信息
     *
     * @param id 主题整合ID
     * @return 结果
     */
    public int deleteByThemeById(Long id);

    /**
     * 查询主题整合列表
     *
     * @param ids 主题整合
     * @return 主题整合集合
     */
    public List<ByTheme> selectByThemeByIds(Long[] ids);

    /**
     * 查询主题整合列表
     *
     * @param ids 主题整合
     * @return 主题整合集合
     */
    public List<ByTheme> selectByThemeByActivityIds(Long[] ids);
}