package com.report.controller;

import com.report.bean.RespInfo;
import com.report.bean.UserInfo;
import com.report.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mingku on 2020/11/20.
 */
@RestController
public class LoginRegController {

    @Autowired
    UserService userService;

    @RequestMapping("/login_error")
    public RespInfo loginError() {
        return new RespInfo("error", "登录失败!");
    }

    @RequestMapping("/login_success")
    public RespInfo loginSuccess() {
        return new RespInfo("success", "登录成功!");
    }

    /**
     * 如果自动跳转到这个页面，说明用户未登录，返回相应的提示即可
     * <p>
     * 如果要支持表单登录，可以在这个方法中判断请求的类型，进而决定返回JSON还是HTML页面
     *
     * @return
     */
    @RequestMapping("/login_page")
    public RespInfo loginPage() {
        return new RespInfo("error", "尚未登录，请登录!");
    }

    @PostMapping("/reg")
    public RespInfo reg(UserInfo user) {
        int result = userService.reg(user);
        if (result == 0) {
            //成功
            return new RespInfo("success", "注册成功!");
        } else if (result == 1) {
            return new RespInfo("error", "用户名重复，注册失败!");
        } else {
            //失败
            return new RespInfo("error", "注册失败!");
        }
    }
}
