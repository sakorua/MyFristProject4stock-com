package com.itheima.stock.service;

import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.pojo.StockBusiness;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author by itheima
 * @Date 2022/1/8
 * @Description 定义操纵股票的服务接口
 */
public interface StockService {
    /**
     * 查询所有股票主营业务信息
     * @return
     */
    R<List<StockBusiness>> findAllBusiness();

    /**
     * 获取国内大盘的实时数据
     * @return
     */
    R<List<InnerMarketDomain>> innerIndexAll();

    /**
     *需求说明: 沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     * @return
     */
    R<List<Map>> sectorAllLimit();

    /**
     * 沪深两市个股涨幅分时行情数据查询，以时间顺序和涨幅查询前10条数据
     * @return
     */
    R<List<Map>> stockIncreaseLimit();

    /**
     * 沪深两市个股行情列表查询 ,以时间顺序和涨幅分页查询
     * @param page 当前页
     * @param pageSize 每页大小
     * @return
     */
    R<PageResult<Map>> stockPage(Integer page, Integer pageSize);

    /**
     * 功能描述：沪深两市涨跌停分时行情数据查询，查询T日每分钟的涨跌停数据（T：当前股票交易日）
     * 		查询每分钟的涨停和跌停的数据的同级；
     * 		如果不在股票的交易日内，那么就统计最近的股票交易下的数据
     * 	 map:
     * 	    upList:涨停数据统计
     * 	    downList:跌停数据统计
     * @return
     */
    R<Map> upDownCount();

    /**
     * 将指定页的股票数据导出到excel表下
     * @param response
     * @param page  当前页
     * @param pageSize 每页大小
     */
    void stockExport(HttpServletResponse response, Integer page, Integer pageSize);

    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     *   map结构示例：
     *      {
     *         "volList": [{"count": 3926392,"time": "202112310930"},......],
     *       "yesVolList":[{"count": 3926392,"time": "202112310930"},......]
     *      }
     * @return
     */
    R<Map> stockTradeVol4InnerMarket();

    /**
     * 功能描述：统计在当前时间下（精确到分钟），股票在各个涨跌区间的数量
     *  如果当前不在股票有效时间内，则以最近的一个有效股票交易时间作为查询时间点；
     * @return
     *  响应数据格式：
     *  {
     *     "code": 1,
     *     "data": {
     *         "time": "2021-12-31 14:58:00",
     *         "infos": [
     *             {
     *                 "count": 17,
     *                 "title": "-3~0%"
     *             },
     *             //...
     *             ]
     *     }
     */
    R<Map> stockUpDownScopeCount();

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    R<List<Map>> stockScreenTimeSharing(String code);

    /**
     * 功能描述：单个个股日K数据查询 ，可以根据时间区间查询数日的K线数据
     * 		默认查询历史20天的数据；
     * @param code 股票编码
     * @return
     */
    R<List<Map>> stockCreenDkLine(String code);


    /**
     * @author SaKoRua
     * @Description //TODO 外盘指数行情数据查询，根据时间和大盘点数降序排序取前4
     * @Date 11:12 AM 2022/1/17
     * @Param
     * @return
     **/
    R<List<Map>> externalIndex();

    
    /**
     * @author SaKoRua
     * @Description //TODO 根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     * @Date 12:47 PM 2022/1/17
     * @Param 
     * @return 
     **/
    R<List<Map>> fuzzyQuery(String searchStr);
}
