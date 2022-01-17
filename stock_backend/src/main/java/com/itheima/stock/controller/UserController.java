package com.itheima.stock.controller;

import com.itheima.stock.service.UserService;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author by itheima
 * @Date 2022/1/8
 * @Description 用户操作的接口层
 */
@RestController
@RequestMapping("/api")
public class UserController {
    /**
     * 仅仅测试
     * @return
     */
//    @GetMapping("/test")
//    public String getData(){
//        return "itheima";
//    }
    /**
     * 注入用户服务bean
     */
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return userService.login(vo);
    }

    /**
     * 生成验证码的服务接口
     * map:
     *   code:随机验证码
     *   rkey:存入redis的全局唯一id，模拟jSessionId
     * @return
     */
    @GetMapping("/captcha")
    public R<Map> generateCaptcha(){
        return userService.generateCaptcha();
    }
}
