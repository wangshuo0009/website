package com.wangshuos.websocket.controller;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wangshuos.common.CookieManager;
import com.wangshuos.common.Result;
import com.wangshuos.websocket.entity.User;
import com.wangshuos.websocket.server.TokenManagerServer;
import com.wangshuos.websocket.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * @ClassName LoginController
 * @Author wangshuo
 * @Date 2024/5/9 13:30
 * @Version 1.0
 **/
@RestController
@RequestMapping("/login")
@Tag(name = "系统登陆")
public class LoginController {
    @Value("definition.kafka")
    private String kafka;

    @Autowired
    private UserService userService;
    @Autowired
    private TokenManagerServer tokenManagerServer;

    @PostMapping("/signIn")
    public Result<?> signIn(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        String account = user.getAccount();
        String password = user.getPassword();
        User one = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getAccount, account)
                .eq(User::getPassword, MD5.create().digestHex(password))
        );
        if (ObjectUtils.isEmpty(one)) {
            return Result.fail("用户名密码错误");
        }
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        CookieManager.setCookie(request, response, account, one.getId().toString(), token);
        tokenManagerServer.setAccountToken(account, token);
        return Result.success("登陆成功", Map.of("token",token));
    }




}
