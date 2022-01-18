package com.itheima.stock.mapper;

import com.itheima.stock.pojo.StockRtInfo;
import com.itheima.stock.vo.resp.R;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Entity com.itheima.stock.pojo.StockRtInfo
 */
@Mapper
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /**
     * 沪深两市个股涨幅分时行情数据查询，以时间顺序和涨幅查询前10条数据
     * @return
     */
    List<Map> stockIncreaseLimit();


    /**
     * 根据时间和涨幅降序排序全表查询
     * @return
     */
    List<Map> stockAll();

    /**
     * 统计指定日期内涨停或者跌停的数据
     * @param avlDate 股票交易时间
     * @param openDate 对应的开盘日期
     * @param flag 1：涨停 0：跌停
     * @return
     */
    List<Map> upDownCount(@Param("avlDate") Date avlDate, @Param("openDate") Date openDate, @Param("flag") Integer flag);

    /**
     * 统计指定时间点下，各个涨跌区间内股票的个数
     * @param avlDate
     * @return
     */
    List<Map> stockUpDownScopeCount(@Param("avlDate") Date avlDate);

    /**
     * 查询指定股票在指定日期下的每分钟的成交流水信息
     * @param code 股票编码
     * @param avlDate 最近的股票有效交易日期
     * @return
     */
    List<Map> stockScreenTimeSharing(@Param("stockCode") String code, @Param("startDate") Date avlDate, @Param("endtDate") Date endDate);

    /**
     *  统计指定股票在指定日期范围内的每天交易数据统计
     * @param code 股票编码
     * @param beginDate 前推的日期时间
     * @return
     */
    List<Map> stockCreenDkLine(@Param("stockCode") String code, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    /**
     * 批量插入股票信息
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<StockRtInfo> list);

    List<Map> getStockCreenDkLineData(@Param("code") String code, @Param("dates") List<Date> dates);

    List<Date> getCloseDates(@Param("code") String code, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    List<Map> fuzzyQuery(@Param("searchStr") String searchStr);

    Map stockDkLine4Week(@Param("code")String code);

    R<Map> stockQuotes(@Param("code") String code, @Param("date") Date date);
}




