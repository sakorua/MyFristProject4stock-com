package com.itheima.stock;

import com.google.gson.Gson;
import com.itheima.stock.config.HttpClientConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * @author by itheima
 * @Date 2022/1/14
 * @Description
 */
@SpringBootTest(classes = HttpClientConfig.class)
public class SpringRestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 测试restTemplate携带url参数发起get请求，并获取字符串类型的响应数据
     */
    @Test
    public void test01(){
        String url="http://localhost:6666/account/getByUserNameAndAddress?userName=houjun&address=shanghai";
        //访问
        /**
         * 参数1：请求的url地址
         * 参数2：响应的数据类型
         */
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        //1.获取响应头
        HttpHeaders headers = entity.getHeaders();
        //[Content-Type:"application/json", Transfer-Encoding:"chunked", Date:"Fri, 14 Jan 2022 01:16:53 GMT", Keep-Alive:"timeout=60", Connection:"keep-alive"]
        System.out.println(headers.toString());
        //2.获取响应状态 404 500 200
        int statucCode = entity.getStatusCodeValue();
        //200
        System.out.println(statucCode);
        //3.获取响应对象 string
        //{"id":1,"userName":"houjun","address":"shanghai"}
        String respData = entity.getBody();
        System.out.println(respData);
        //字符串转对象
        Account account = new Gson().fromJson(respData, Account.class);
        System.out.println(account);
    }

    @Test
    public void test02(){
        String url="http://localhost:6666/account/getByUserNameAndAddress?userName=houjun&address=shanghai";
        //访问
        /**
         * 参数1：请求的url地址
         * 参数2：响应的数据类型
         */
        ResponseEntity<Account> entity = restTemplate.getForEntity(url, Account.class);
        //1.获取响应头
        HttpHeaders headers = entity.getHeaders();
        //[Content-Type:"application/json", Transfer-Encoding:"chunked", Date:"Fri, 14 Jan 2022 01:16:53 GMT", Keep-Alive:"timeout=60", Connection:"keep-alive"]
        System.out.println(headers.toString());
        //2.获取响应状态 404 500 200
        int statucCode = entity.getStatusCodeValue();
        //200
        System.out.println(statucCode);
        //3.获取响应对象 Account
        Account body = entity.getBody();
        System.out.println(body);
    }

    @Test
    public void test03(){
        String url="http://localhost:6666/account/getByUserNameAndAddress?userName=houjun&address=shanghai";
        //访问
        /**
         * 参数1：请求的url地址
         * 参数2：响应的数据类型
         */
        Account respBody = restTemplate.getForObject(url, Account.class);
        System.out.println(respBody);

    }

    /**
     * 发起请求，请求头中携带指定的参数，供访问接口获取
     * http://localhost:6666/account/getHeader
     */
    @Test
    public void test4(){
        String url="http://localhost:6666/account/getHeader";
        //1.构建请求头对象
        HttpHeaders headers = new HttpHeaders();
        headers.add("userName","lizemin");
        //2.构建发起请求的实体对象
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        //3.发起请求
        /**
         * 参数1：请求url
         * 参数2：请求方式
         * 参数3：请求对象
         * 参数4：响应的数据类型
         */
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        //获取响应对象
        System.out.println(resp.getBody());


    }

    /**
     * 模拟form表单提交数据
     */
    @Test
    public void post4Form(){
        String url="http://localhost:6666/account/addAccount";
        //组装参数
        //设置请求头中指定form的请求方式
        //设置请求头，指定请求数据方式
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded");
        //设置请求体的数据
        LinkedMultiValueMap<String, Object> bodyData = new LinkedMultiValueMap<>();
        bodyData.add("id",10);
        bodyData.add("userName","wangke");
        bodyData.add("address","sh");
        //封装请求对象
        HttpEntity<LinkedMultiValueMap<String, Object>> reqEntity = new HttpEntity<>(bodyData, headers);
        //发起请求
        ResponseEntity<Account> resp = restTemplate.exchange(url, HttpMethod.POST, reqEntity, Account.class);
        Account body = resp.getBody();
        System.out.println(body);
    }

    /**
     * post请求模拟ajax发送json格式字符串到后台
     */
    @Test
    public void test4PostWithJson(){
        String url="http://localhost:6666/account/updateAccount";
        //组装一个json格式字符串
        String jsonReq="{\"address\":\"上海\",\"id\":\"1\",\"userName\":\"zhangsan\"}";
        //设置请求头，指定请求数据方式
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/json;charset=utf-8");
        //组装请求对象
        HttpEntity<String> entity = new HttpEntity<>(jsonReq, headers);
        //发起请求
        ResponseEntity<Account> resp = restTemplate.exchange(url, HttpMethod.POST, entity, Account.class);
        Account body = resp.getBody();
        System.out.println(body);
    }

    /**
     * 测试获取指定接口响应的cookie
     */
    @Test
    public void getCookie(){
        String url="http://localhost:6666/account/getCookie";
        //访问接口
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        //获取cookie
        List<String> cookies = entity.getHeaders().get("Set-Cookie");
        System.out.println(cookies);
        //获取响应数据
        String body = entity.getBody();
        System.out.println(body);

    }

















    /**
     * @author by itheima
     * @Date 2021/12/27
     * @Description
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Account {

        private Integer id;

        private String userName;

        private String address;

    }


}
