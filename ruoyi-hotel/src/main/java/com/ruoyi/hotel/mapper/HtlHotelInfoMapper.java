package com.ruoyi.hotel.mapper;

import java.util.List;
import com.ruoyi.hotel.domain.HtlHotelInfo;
import org.apache.ibatis.annotations.Select;

/**
 * 酒店信息Mapper接口
 * 
 * @author sucheng
 * @date 2020-11-22
 */
public interface HtlHotelInfoMapper 
{
    @Select("select t.city_id from htl_hotel_info t group by t.city_id")
    public List<Long> selectAllLocationList();

    /**
     * 查询酒店信息
     * 
     * @param hotelId 酒店信息ID
     * @return 酒店信息
     */
    public HtlHotelInfo selectHtlHotelInfoById(Long hotelId);

    /**
     * 通过用户ID查询酒店信息
     * 
     * @param userId 用户ID
     * @return 酒店信息
     */
    public HtlHotelInfo selectHtlHotelInfoByUserId(Long userId);
    
    /**
     * 查询酒店信息列表
     * 
     * @param htlHotelInfo 酒店信息
     * @return 酒店信息集合
     */
    public List<HtlHotelInfo> selectHtlHotelInfoList(HtlHotelInfo htlHotelInfo);

    /**
     * 新增酒店信息
     * 
     * @param htlHotelInfo 酒店信息
     * @return 结果
     */
    public int insertHtlHotelInfo(HtlHotelInfo htlHotelInfo);

    /**
     * 修改酒店信息
     * 
     * @param htlHotelInfo 酒店信息
     * @return 结果
     */
    public int updateHtlHotelInfo(HtlHotelInfo htlHotelInfo);

    /**
     * 删除酒店信息
     * 
     * @param hotelId 酒店信息ID
     * @return 结果
     */
    public int deleteHtlHotelInfoById(Long hotelId);

    /**
     * 批量删除酒店信息
     * 
     * @param hotelIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteHtlHotelInfoByIds(Long[] hotelIds);
}
