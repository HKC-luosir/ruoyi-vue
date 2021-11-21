package com.ruoyi.web.controller.yunbookmark;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.bookmark.domain.SqBookmark;
import com.ruoyi.bookmark.pojo.SqBookmarkReq;
import com.ruoyi.bookmark.service.ISqBookmarkService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.bookmarkhtml.ImportHtml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 书签管理Controller
 *
 * @author wanghao
 * @date 2020-08-02
 */
@RestController
@RequestMapping("/bookmark/bookmark")
public class SqBookmarkController extends BaseController
{
    public final static Logger logger =  LoggerFactory.getLogger(SqBookmarkController.class);
    @Autowired
    private ISqBookmarkService sqBookmarkService;


    /**
     *  删除重复的书签
     */
    @GetMapping("/bookmarkDeleteRepetition")
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:deleterepetition')")
    public AjaxResult bookmarkDeleteRepetition() {
        return AjaxResult.success(sqBookmarkService.bookmarkDeleteRepetition(getAuthUser().getUserId()));
    }
    /**
     *获取所有重复的书签
     */
    @GetMapping("/bookmarkRepetition")
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:listrepetition')")
    public AjaxResult bookmarkRepetition() {
        startPage();
        List<SqBookmark> list = sqBookmarkService.bookmarkRepetition(getAuthUser().getUserId());
        return AjaxResult.success(list);
    }
    /**
     * 书签设置星标 取消星标
     */
    @GetMapping("/updateBookmarkStarById")
    public AjaxResult updateBookmarkStarById(Long bookmarkId,Integer bookmarkStr) {
        return toAjax(sqBookmarkService.updateBookmarkStarById(getAuthUser().getUserId(),bookmarkId,bookmarkStr));
    }

    /**
     * 通过url 查询用户 是否已经添加了此书签
     *
     * 如果有多条 返回最新的一条
     * @return
     */
    @GetMapping("/selectByUrlUserID")
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:url')")
    public AjaxResult selectByUrlUserID(String url) {
        startPage();
        List<SqBookmark> list = sqBookmarkService.selectByUrlUserID(url,getAuthUser().getUserId());
        if (list!=null&&!list.isEmpty()){
            SqBookmark sqBookmark = list.get(0);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title",sqBookmark.getTitle());
            jsonObject.put("bookmarkId",sqBookmark.getBookmarkId());
            jsonObject.put("menuId",sqBookmark.getMenuId());
            jsonObject.put("description",sqBookmark.getDescription());
            jsonObject.put("label",sqBookmark.getLabel());
            jsonObject.put("seeYouLater",sqBookmark.getSeeYouLater());
            jsonObject.put("url",sqBookmark.getUrl());
            return new AjaxResult(200,jsonObject.toString());
        }
        return new AjaxResult(200,"");
    }


    /**
     * 查询用户栏目下的书签
     * @return
     */
    @GetMapping("/selectBymenuIdUserID")
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:listsousou')")
    public TableDataInfo selectBymenuIdUserID(Long menuId,Integer sort,String sousuo,Integer bookmarkStar,Integer start) {
        startPage();
        List<SqBookmark> list = sqBookmarkService.selectBymenuIdUserID(menuId,getAuthUser().getUserId(),sort,sousuo,bookmarkStar,start);
        return getDataTable(list);
    }
    /**
     * 回收站
     * @return
     */
    @GetMapping("/selectBydelete")
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:recycle')")
    public TableDataInfo selectBydelete() {
        startPage();
        List<SqBookmark> list = sqBookmarkService.selectBydelete(getAuthUser().getUserId());
        return getDataTable(list);
    }

    /**
     * 用户全部书签
     * @return
     */
    @GetMapping("/selectByUseridList")
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:list')")
    public TableDataInfo selectByUseridList() {
        startPage();
        List<SqBookmark> list = sqBookmarkService.selectByUseridList(getAuthUser().getUserId());
        return getDataTable(list);
    }

    /**
     * 查询书签管理列表 聚合>> 星标 稍后看 最新书签
     */
    @GetMapping("/listByUserAndPolymerization")
    public TableDataInfo listByUserAndPolymerization(SqBookmark sqBookmark)
    {
        sqBookmark.setUserid(getAuthUser().getUserId());
        startPage();
        List<SqBookmark> list = sqBookmarkService.listByUserAndPolymerization(sqBookmark);
        return getDataTable(list);
    }


    /**
     * 查询书签管理列表
     */
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqBookmark sqBookmark)
    {
        startPage();
        List<SqBookmark> list = sqBookmarkService.selectSqBookmarkList(sqBookmark);
        return getDataTable(list);
    }

    /**
     * 导出书签管理列表
     */
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:export')")
    @Log(title = "书签管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SqBookmark sqBookmark)
    {
        List<SqBookmark> list = sqBookmarkService.selectSqBookmarkList(sqBookmark);
        ExcelUtil<SqBookmark> util = new ExcelUtil<SqBookmark>(SqBookmark.class);
        return util.exportExcel(list, "bookmark");
    }

    /**
     * 获取书签管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:query')")
    @GetMapping(value = "/{bookmarkId}")
    public AjaxResult getInfo(@PathVariable("bookmarkId") Long bookmarkId)
    {
        return AjaxResult.success(sqBookmarkService.selectSqBookmarkById(bookmarkId));
    }

    /**
     * 新增书签管理
     */
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:add')")
    @Log(title = "书签管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SqBookmark sqBookmark)
    {
        sqBookmark.setUserid(getAuthUser().getUserId());
        if(sqBookmark.getBookmarkId()!=null){
            //修改
            sqBookmarkService.updateSqBookmark(sqBookmark);
            return new AjaxResult(200,sqBookmark.getBookmarkId().toString());
        }

        String menuId = sqBookmarkService.insertSqBookmark(sqBookmark);
        return new AjaxResult(200,menuId);
    }

    /**
     * 修改书签管理
     */
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:edit')")
    @Log(title = "书签管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SqBookmark sqBookmark)
    {
        sqBookmark.setUserid(getAuthUser().getUserId());
        return toAjax(sqBookmarkService.updateSqBookmark(sqBookmark));
    }

    /**
     * 删除书签管理
     */
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:remove')")
    @Log(title = "书签管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bookmarkIds}")
    public AjaxResult remove(@PathVariable Long bookmarkIds)
    {
        return toAjax(sqBookmarkService.deleteSqBookmarkById(bookmarkIds,getAuthUser().getUserId()));
    }

    /**
     * 删除书签 根据URL删除
     */
    @PreAuthorize("@ss.hasPermi('bookmark:bookmark:common:remove')")
	@DeleteMapping("/deleteByurl")
    public AjaxResult deleteByurl(String url)
    {
        return AjaxResult.success("success");
//        return sqBookmarkService.deleteByUrl(getAuthUser().getUserId(),url);
    }


    /**
     * 用户-根据标签查询书签分页
     */
    @GetMapping(value = "/listByTag")
    public TableDataInfo listByTag(SqBookmarkReq sqBookmarkReq){
        sqBookmarkReq.setUserid(getAuthUser().getUserId());
        startPage();
        return getDataTable(sqBookmarkService.getlistByTag(sqBookmarkReq));
    }
}