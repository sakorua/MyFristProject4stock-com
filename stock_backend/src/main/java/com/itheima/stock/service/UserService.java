package com.itheima.stock.service;

import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;

import java.util.Map;

/**
 * @author by itheima
 * @Date 2022/1/8
 * @Description 定义用户服务接口
 */
public interface UserService {
    /**
     * 用户登录方法
     * @param vo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo vo);

    /**
     * 生成验证码的服务接口
     * map:
     *   code:随机验证码
     *   rkey:存入redis的全局唯一id，模拟jSessionId
     * @return
     */
    R<Map> generateCaptcha();

}
