package com.itheima.stock.config;

import com.itheima.stock.common.TaskThreadPoolInfo;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author by itheima
 * @Date 2022/1/15
 * @Description
 */
@Configuration
@EnableConfigurationProperties(TaskThreadPoolInfo.class)
public class TaskExecutePool {
    private TaskThreadPoolInfo taskThreadPoolInfo;

    public TaskExecutePool(TaskThreadPoolInfo taskThreadPoolInfo) {
        this.taskThreadPoolInfo = taskThreadPoolInfo;
    }

    /**
     * 定义线程池bean
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        //构建线程池对象
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        taskExecutor.setCorePoolSize(taskThreadPoolInfo.getCorePoolSize());
        //设置最大线程数
        taskExecutor.setMaxPoolSize(taskThreadPoolInfo.getMaxPoolSize());
        //设置空闲线程的超时时间，超过指定时间，则被回收
        taskExecutor.setKeepAliveSeconds(taskThreadPoolInfo.getKeepAliveSeconds());
        //设置队列长度
        taskExecutor.setQueueCapacity(taskThreadPoolInfo.getQueueCapacity());
        //设置核心线程数是否超时时被回收
        //taskExecutor.setAllowCoreThreadTimeOut(true);
        //设置线程名称前缀
        taskExecutor.setThreadNamePrefix("stock-thread-");
        //设置任务拒绝策略
        taskExecutor.setRejectedExecutionHandler(new StockRejectedExecutionHandler());
        return taskExecutor;
    }




}
