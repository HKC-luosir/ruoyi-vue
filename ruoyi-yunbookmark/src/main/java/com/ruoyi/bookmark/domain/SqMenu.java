package com.ruoyi.bookmark.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 书签菜单对象 sq_menu
 *
 * @author wanghao
 * @date 2020-08-06
 */
@Table(name="sq_menu")
public class SqMenu
{
    private static final long serialVersionUID = 1L;
    /** ID 返回自增长主键 */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "menu_id")
    private Long menuId;

    /** 归属用户ID */
    @Excel(name = "归属用户ID")
    @Column(name = "user_id")
    private Long userId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    @Column(name = "menu_name")
    private String menuName;

    /** 菜单url */
    @Excel(name = "菜单url")
    @Column(name = "menu_url")
    private String menuUrl;

    /** 目录串 */
    @Excel(name = "目录串")
    @Column(name = "menu_uplink_series")
    private String menuUplinkSeries;

    /** 菜单图标 */
    @Excel(name = "菜单图标")
    @Column(name = "menu_order")
    private String menuIcon;

    /** 菜单顺序 */
    @Excel(name = "菜单顺序")
    @Column(name = "menu_order")
    private Integer menuOrder;

    @Column(name = "parent_id")
    private Long parentId;

    /** 下级书签数量 */

    @Column(name = "bookmark_count")
    private Integer bookmarkCount;

    /** 是否有下级 0无 1有*/
    @Column(name = "subordinate")
    private Integer subordinate;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;


    public String getMenuUplinkSeries() {
        return menuUplinkSeries;
    }

    public void setMenuUplinkSeries(String menuUplinkSeries) {
        this.menuUplinkSeries = menuUplinkSeries;
    }

    public Integer getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(Integer bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public Integer getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(Integer subordinate) {
        this.subordinate = subordinate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    public Long getMenuId()
    {
        return menuId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getMenuName()
    {
        return menuName;
    }
    public void setMenuUrl(String menuUrl)
    {
        this.menuUrl = menuUrl;
    }

    public String getMenuUrl()
    {
        return menuUrl;
    }
    public void setMenuIcon(String menuIcon)
    {
        this.menuIcon = menuIcon;
    }

    public String getMenuIcon()
    {
        return menuIcon;
    }
    public void setMenuOrder(Integer menuOrder)
    {
        this.menuOrder = menuOrder;
    }

    public Integer getMenuOrder()
    {
        return menuOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("menuId", getMenuId())
            .append("userId", getUserId())
            .append("menuName", getMenuName())
            .append("menuUrl", getMenuUrl())
            .append("menuIcon", getMenuIcon())
            .append("parentId", getParentId())
            .append("menuOrder", getMenuOrder())
            .append("bookmarkCount", getBookmarkCount())
            .append("subordinate", getSubordinate())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("menuUplinkSeries", getMenuUplinkSeries())
            .toString();
    }
    public SqMenu(){
        super();
    }

    public SqMenu( Long userId, String menuName, Long parentId,String menuIcon) {
        this.userId = userId;
        this.menuName = menuName;
        this.parentId = parentId;
        this.menuIcon = menuIcon;
    }

    public SqMenu( Long menuId,String menuUplinkSeries) {
        this.menuId = menuId;
        this.menuUplinkSeries = menuUplinkSeries;
    }
    public SqMenu( Long parentId,Long userId) {
        this.parentId = parentId;
        this.userId = userId;
    }
    public SqMenu( Long menuId,Integer subordinate) {
        this.menuId = menuId;
        this.subordinate = subordinate;
    }
}