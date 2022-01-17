package com.itheima.stock.service.impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.google.common.base.Strings;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.service.UserService;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author by itheima
 * @Date 2022/1/8
 * @Description 定义用户服务接口实现
 */
@Service("userService")
public class UserServiceImpl implements UserService {



    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 注入密码工具bean
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 用户登录方法
     * @param vo
     * @return
     */
    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        //1.判断用户名和密码是否存在 补充：如果随机校验码或者sessionId:rkey必须存在，否则校验失败
        if (vo==null || Strings.isNullOrEmpty(vo.getUsername()) || Strings.isNullOrEmpty(vo.getPassword()) ||
            Strings.isNullOrEmpty(vo.getCode()) ||  Strings.isNullOrEmpty(vo.getRkey())) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //补充：先redis下的验证码校验 因为速度足够块
        String rCode = (String) redisTemplate.opsForValue().get(vo.getRkey());
        if (rCode==null || !rCode.equals(vo.getCode())) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //即使删除redis下的数据 || 如果不删除，超过时，也会失效清除
        redisTemplate.delete(vo.getRkey());
        //2.根据用户名查询用户信息，并判断用户是否存在
        SysUser user= sysUserMapper.findUserByUserName(vo.getUsername());
        if (user==null) {
            return R.error(ResponseCode.SYSTEM_USERNAME_NOT_EXISTS.getMessage());
        }
        //3.判断查询的db中的密码与前端传入的密码进行校验
        boolean flag = passwordEncoder.matches(vo.getPassword(), user.getPassword());
        if (!flag) {
            return R.error(ResponseCode.SYSTEM_PASSWORD_ERROR.getMessage());
        }
        //成功，响应数据
        LoginRespVo repVo = new LoginRespVo();
        //属性值复制，前提：两个对象内的属性相同
        BeanUtils.copyProperties(user,repVo);
        return R.ok(repVo);
    }

    /**
     * 生成验证码的服务接口
     * map:
     *   code:随机验证码
     *   rkey:存入redis的全局唯一id，模拟jSessionId
     * @return
     */
    @Override
    public R<Map> generateCaptcha() {
        //1.生成随机校验码
        String checkCode = RandomStringUtils.randomNumeric(4);
        //2.生成全局唯一id，模拟sessionId
        long rKey = idWorker.nextId();
        //3.模拟存储session下,失效时间1分钟，我们当前存入redis下
        redisTemplate.opsForValue().set(rKey+"",checkCode,5, TimeUnit.MINUTES);
        //4.组装响应数据
        Map<String, String> info = new HashMap<>();
        info.put("code",checkCode);
        info.put("rkey",rKey+"");
        return R.ok(info);
    }
}
