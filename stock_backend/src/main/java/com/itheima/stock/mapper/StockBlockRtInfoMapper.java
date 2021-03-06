package com.itheima.stock.mapper;

import com.itheima.stock.pojo.StockBlockRtInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.itheima.stock.pojo.StockBlockRtInfo
 */
@Mapper
public interface StockBlockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBlockRtInfo record);

    int insertSelective(StockBlockRtInfo record);

    StockBlockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBlockRtInfo record);

    int updateByPrimaryKey(StockBlockRtInfo record);

    /**
     * 沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     * @return
     */
    List<Map> sectorAllLimit();

    /**
     * 批量插入板块数据
     * @param items
     * @return
     */
    int insertBatch(@Param("items") List<StockBlockRtInfo> items);
}




