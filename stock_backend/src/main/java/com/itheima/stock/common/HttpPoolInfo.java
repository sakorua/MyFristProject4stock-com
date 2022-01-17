package com.itheima.stock.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author by itheima
 * @Date 2022/1/15
 * @Description
 */
@ConfigurationProperties(prefix = "http.pool")
@Data
public class HttpPoolInfo {
    //最大连接数
    private Integer maxConnectionTotal;
    //指定服务每次能并行接收的请求数量
    private Integer routeMaxCount;
    //空闲连接超时时间超时后小于连接会被回收
    private Integer connectionIdleTimeOut;
    //请求失败重试次数
    private Integer retryCount;
    //连接超时时间
    private Integer connectionTimeOut;
    //读取数据超时时间
    private Integer readTimeOut;
    //连接池不够用时，等待超时时间
    private Integer connectionRequestTimeOut;
}
