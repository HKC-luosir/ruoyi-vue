package com.ruoyi.project.benyi.mapper;

import java.util.List;
import com.ruoyi.project.benyi.domain.ByTrainVideoScore;

/**
 * 培训视频评分Mapper接口
 * 
 * @author tsbz
 * @date 2020-06-01
 */
public interface ByTrainVideoScoreMapper 
{
    /**
     * 查询培训视频评分
     * 
     * @param id 培训视频评分ID
     * @return 培训视频评分
     */
    public ByTrainVideoScore selectByTrainVideoScoreById(Long id);

    /**
     * 查询培训视频评分列表
     * 
     * @param byTrainVideoScore 培训视频评分
     * @return 培训视频评分集合
     */
    public List<ByTrainVideoScore> selectByTrainVideoScoreList(ByTrainVideoScore byTrainVideoScore);

    /**
     * 新增培训视频评分
     * 
     * @param byTrainVideoScore 培训视频评分
     * @return 结果
     */
    public int insertByTrainVideoScore(ByTrainVideoScore byTrainVideoScore);

    /**
     * 修改培训视频评分
     * 
     * @param byTrainVideoScore 培训视频评分
     * @return 结果
     */
    public int updateByTrainVideoScore(ByTrainVideoScore byTrainVideoScore);

    /**
     * 删除培训视频评分
     * 
     * @param id 培训视频评分ID
     * @return 结果
     */
    public int deleteByTrainVideoScoreById(Long id);

    /**
     * 批量删除培训视频评分
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteByTrainVideoScoreByIds(Long[] ids);
}