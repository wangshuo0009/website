package com.wangshuos.common;

import com.wangshuos.common.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;


/**
 * @ClassName CookieManager
 * @Author wangshuo
 * @Date 2024/5/15 11:25
 * @Version 1.0
 **/
public class CookieManager {

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String account, String userId, String token) {
        // 创建一个 Cookie 存储用户帐号
        ResponseCookie accountCookie = ResponseCookie.from(CommonConstant.X_USER_ACCOUNT, account)
                //.maxAge(-1)               // 浏览器关闭，则删除 Cookie
                .secure(true)                          // false 可以在 HTTP 协议中传输
                .httpOnly(true)                        // true 的话Javascript 不能读写
                //.domain("localhost")                    // 提交 cookie 的域
                .path("/")                               // 提交 cookie 的path
                .sameSite(Cookie.SameSite.STRICT.attributeValue())    // 设置 SameSite 为 NONE
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE,accountCookie.toString());

        ResponseCookie userIdCookie = ResponseCookie.from(CommonConstant.X_USER_ID, userId)
                //.maxAge(-1)               // 浏览器关闭，则删除 Cookie
                .secure(true)                          // false 可以在 HTTP 协议中传输
                .httpOnly(true)                        // true 的话Javascript 不能读写
                //.domain("localhost")                    // 提交 cookie 的域
                .path("/")                              // 提交 cookie 的path
                .sameSite(Cookie.SameSite.STRICT.attributeValue())    // 设置 SameSite 为 NONE
                .build();
        // 将 Cookie 添加到响应中
        response.addHeader(HttpHeaders.SET_COOKIE,userIdCookie.toString());

        ResponseCookie userToken = ResponseCookie.from(CommonConstant.X_Token, token)
                //.maxAge(-1)               // 浏览器关闭，则删除 Cookie
                .secure(true)                          // false 可以在 HTTP 协议中传输
                .httpOnly(true)                        // true 的话Javascript 不能读写
                //.domain("localhost")                    // 提交 cookie 的域
                .path("/")                              // 提交 cookie 的path
                .sameSite(Cookie.SameSite.STRICT.attributeValue())    // 设置 SameSite 为 NONE
                .build();
        // 将 Cookie 添加到响应中
        response.addHeader(HttpHeaders.SET_COOKIE,userToken.toString());

    }
}
