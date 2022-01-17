package com.itheima.stock.config;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author by itheima
 * @Date 2022/1/15
 * @Description 自定义线程池任务的拒绝策略
 */
@Slf4j
public class StockRejectedExecutionHandler implements RejectedExecutionHandler {
    /**
     *
     * @param r 当前任务对象
     * @param executor 当前线程池对象
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
       // r instanceof xxxx
        log.info("股票信息采集出现异常......");
    }
}
