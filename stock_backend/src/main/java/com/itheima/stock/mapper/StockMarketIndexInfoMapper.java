package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.pojo.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Entity com.itheima.stock.pojo.StockMarketIndexInfo
 */
@Mapper
public interface StockMarketIndexInfoMapper {

    /**
     * 根据注定的id集合和日期查询大盘数据
     * @param ids 大盘id集合
     * @param lastDate 对应日期
     * @return
     */
     List<InnerMarketDomain> selectByIdsAndDate(@Param("ids") List<String> ids, @Param("lastDate") Date lastDate);

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    /**
     * 查询指定大盘下的指定日期下小于等于指定时间的数据，结果包含：每分钟内，整体大盘的交易量的统计
     * @param marketIds 股票大盘的编码code集合
     * @param openDate 开盘时间
     * @param tStr 日期时间，精确到秒
     * @return
     */
    List<Map> stockTradeVolCount(@Param("marketIds") List<String> marketIds, @Param("openDate") Date openDate , @Param("stockDateTime") Date tStr);

    /**
     * 批量插入大盘数据
     * @param list 大盘实体对象集合
     * @return
     */
    int insertBatch(@Param("list") ArrayList<StockMarketIndexInfo> list);


    /**
     * @author SaKoRua
     * @Description //TODO 外盘指数行情数据查询，根据时间和大盘点数降序排序取前4
     * @Date 11:12 AM 2022/1/17
     * @Param
     * @return
     **/
    List<Map> selectByIdsAndDate4outer(@Param("ids") List<String> outer, @Param("lastDate") Date date);
}




