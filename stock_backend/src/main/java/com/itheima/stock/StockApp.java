package com.itheima.stock;

import com.itheima.stock.common.StockInfoConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author by itheima
 * @Date 2022/1/8
 * @Description 指定工程启动类
 */
@SpringBootApplication
//@MapperScan("com.itheima.stock.mapper")
@EnableConfigurationProperties(StockInfoConfig.class)
public class StockApp {

    public static void main(String[] args) {
        SpringApplication.run(StockApp.class,args);
    }
}
