package com.ruoyi.project.benyi.mapper;

import java.util.List;
import com.ruoyi.project.benyi.domain.ByTrainVideoFeedback;

/**
 * 培训视频评价反馈Mapper接口
 * 
 * @author tsbz
 * @date 2020-06-01
 */
public interface ByTrainVideoFeedbackMapper 
{
    /**
     * 查询培训视频评价反馈
     * 
     * @param id 培训视频评价反馈ID
     * @return 培训视频评价反馈
     */
    public ByTrainVideoFeedback selectByTrainVideoFeedbackById(Long id);

    /**
     * 查询培训视频评价反馈列表
     * 
     * @param byTrainVideoFeedback 培训视频评价反馈
     * @return 培训视频评价反馈集合
     */
    public List<ByTrainVideoFeedback> selectByTrainVideoFeedbackList(ByTrainVideoFeedback byTrainVideoFeedback);

    /**
     * 新增培训视频评价反馈
     * 
     * @param byTrainVideoFeedback 培训视频评价反馈
     * @return 结果
     */
    public int insertByTrainVideoFeedback(ByTrainVideoFeedback byTrainVideoFeedback);

    /**
     * 修改培训视频评价反馈
     * 
     * @param byTrainVideoFeedback 培训视频评价反馈
     * @return 结果
     */
    public int updateByTrainVideoFeedback(ByTrainVideoFeedback byTrainVideoFeedback);

    /**
     * 删除培训视频评价反馈
     * 
     * @param id 培训视频评价反馈ID
     * @return 结果
     */
    public int deleteByTrainVideoFeedbackById(Long id);

    /**
     * 批量删除培训视频评价反馈
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteByTrainVideoFeedbackByIds(Long[] ids);
}