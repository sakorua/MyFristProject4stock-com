package com.itheima.stock.common;

import com.itheima.stock.utils.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author by itheima
 * @Date 2022/1/8
 * @Description 定义公共的工具类配置
 */
@Configuration
public class CommonConfig {

    /**
     * 定义密码加密器bean
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * 定义全局id生成器bean
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(2,2);
    }

}
