package com.itheima.stock.config;

import com.itheima.stock.common.HttpPoolInfo;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author by itheima
 * @Date 2022/1/14
 * @Description 配置http相关的bean
 */
@Configuration
@EnableConfigurationProperties(HttpPoolInfo.class)
public class HttpClientConfig {

    private HttpPoolInfo httpPoolInfo;

    /**
     * 通过构造器注入HttpPoolInfo bean对象
     * @param httpPoolInfo
     */
    public HttpClientConfig(HttpPoolInfo httpPoolInfo) {
        this.httpPoolInfo = httpPoolInfo;
    }

    /**
     * 定义RestTemplate bean的配置
     * @return
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(clientHttpRequestFactory());
//        return new RestTemplate();
    }


    /**
     * 配置http请求工厂bean
     * @return
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(){
        //获取httpClient对象
        CloseableHttpClient client =
                HttpClientBuilder.create()
                        //设置最大连接数
                        .setMaxConnTotal(httpPoolInfo.getMaxConnectionTotal())
                        //路由到单台机器的最大数量
                        .setMaxConnPerRoute(httpPoolInfo.getRouteMaxCount())
                        //空闲连接超时时间
                        .evictIdleConnections(httpPoolInfo.getConnectionIdleTimeOut(), TimeUnit.MILLISECONDS)
                        //设置失败重试次数
                        .setRetryHandler(new DefaultHttpRequestRetryHandler(httpPoolInfo.getRetryCount(), true))
                        .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                        .build();
        //构建连接对象
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(client);
        //设置连接最大超时时间
        clientHttpRequestFactory.setConnectTimeout(httpPoolInfo.getConnectionTimeOut());
        //设置读取数据超时时间
        clientHttpRequestFactory.setReadTimeout(httpPoolInfo.getReadTimeOut());
        //设置等待获取连接池超时时间
        clientHttpRequestFactory.setConnectionRequestTimeout(httpPoolInfo.getConnectionRequestTimeOut());
        //设置是否缓存请求体（如果存在大量post请求，且请求参数一致，则可设置为true）
        clientHttpRequestFactory.setBufferRequestBody(false);
        return clientHttpRequestFactory;
    }









}
