package com.itheima.stock.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author by itheima
 * @Date 2022/1/15
 * @Description
 */
@Data
@ConfigurationProperties(prefix = "task.pool")
public class TaskThreadPoolInfo {
    /**
     *  核心线程数（获取硬件）：线程池创建时候初始化的线程数
     */
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer keepAliveSeconds;
    private Integer queueCapacity;
}
