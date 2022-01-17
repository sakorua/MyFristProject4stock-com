package com.itheima.stock.xxljob;

import com.itheima.stock.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author by itheima
 * @Date 2022/1/15
 * @Description 拉去股票数据的定义服务
 */
@Component
public class StockJob {

    /**
     * 注入股票定时任务服务bean
     */
    @Autowired
    private StockTimerTaskService stockTimerTaskService;


    /**
     * 定义定时任务，采集国内大盘数据
     */
    @XxlJob("getStockInnerMarketInfos")
    public void getStockInnerMarketInfos(){
        stockTimerTaskService.getInnerMarketInfo();
    }

    /**
     * 定时采集A股数据
     */
    @XxlJob("getStockInfos")
    public void getStockInfos(){
        stockTimerTaskService.getStockRtIndex();
    }

    /**
     * 板块定时任务
     */
    @XxlJob("getStockBlockInfoTask")
    public void getStockBlockInfoTask(){
        stockTimerTaskService.getStockSectorRtIndex();
    }



    @XxlJob("testStock")
    public void testStock(){
        System.out.println(System.currentTimeMillis()+"执行。。。。。");
    }


    /**
     * @author SaKoRua
     * @Description //TODO 测试定时任务
     * @Date 10:21 AM 2022/1/17
     * @Param
     * @return
     **/

    @XxlJob("runTest")
    public void runTest(){
        stockTimerTaskService.testTimerTask();
    }

}
