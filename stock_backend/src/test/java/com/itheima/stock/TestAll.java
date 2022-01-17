package com.itheima.stock;

import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.utils.DateTimeUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author by itheima
 * @Date 2022/1/8
 * @Description
 */
@SpringBootTest
public class TestAll {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test01(){
        SysUser user = sysUserMapper.selectByPrimaryKey(1237361915165020161L);
        System.out.println(user);
    }

    @Test
    public void testPwd(){
        String pwd="1234";
        //加密
//        String encode = passwordEncoder.encode(pwd);
        //$2a$10$WAWV.QEykot8sHQi6FqqDOAnevkluOZJqZJ5YPxSnVVWqvuhx88Ha
//        System.out.println(encode);
        /*
            matches()匹配明文密码和加密后密码是否匹配，如果匹配，返回true，否则false
            just test
         */
        boolean flag = passwordEncoder.matches(pwd, "$2a$10$WAWV.QEykot8sHQi6FqqDOAnevkluOZJqZJ5YPxSnVVWqvuhx88Ha");
        System.out.println(flag);
    }

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("name","zhangsan");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }

    @Test
    public void testApacheLang(){
        //纯字母，包含大小写
        String random2 = RandomStringUtils.randomAlphabetic(6);
        System.out.println(random2);
        //生成字母和数字
        String random3 = RandomStringUtils.randomAlphanumeric(6);
        System.out.println(random3);
        //生成随机数字 0-9
        String random4 = RandomStringUtils.randomNumeric(6);
        System.out.println(random4);
    }

    @Test
    public void testJode(){
        //获取指定时间下最近的股票有效交易时间
        String leastDateTime = DateTimeUtil.getLastDateString4Stock(DateTime.now());
        System.out.println(leastDateTime);
    }


}
