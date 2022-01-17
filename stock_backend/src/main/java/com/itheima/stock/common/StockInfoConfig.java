package com.itheima.stock.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author by itheima
 * @Date 2022/1/9
 * @Description
 */
@Data
@ConfigurationProperties(prefix = "stock")
public class StockInfoConfig {
    //a股大盘ID集合
    private List<String> inner;
    //外盘ID集合
    private List<String> outer;
    //股票涨幅区间定义集合
    private List<String> upDownRange;
    //大盘参数获取url
    private String marketUrl;
    //板块参数获取url
    private String blockUrl;
}
