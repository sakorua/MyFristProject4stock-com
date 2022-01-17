package com.itheima.stock.service;

/**
 * @author by itheima
 * @Date 2022/1/14
 * @Description 定义采集股票数据的定时任务的服务接口
 */
public interface StockTimerTaskService {

    /**
     * 获取国内大盘的实时数据信息
     */
    void getInnerMarketInfo();

    /**
     * 定义获取分钟级A股股票数据
     */
    void getStockRtIndex();


    /**
     * 获取板块数据
     */
    void getStockSectorRtIndex();


    /**
     * @author SaKoRua
     * @Description //TODO 测试定时任务
     * @Date 10:21 AM 2022/1/17
     * @Param
     * @return
     **/
    void testTimerTask();
}
