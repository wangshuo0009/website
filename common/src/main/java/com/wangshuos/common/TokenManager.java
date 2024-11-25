package com.wangshuos.common;

import com.wangshuos.common.constant.CommonConstant;
import com.wangshuos.common.exception.NotLoggedInException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * @Author wangshuo
 * @Description Token管理类，用于管理用户的Token及其有效期
 * @Date 2024/5/9 14:07
 **/
public class TokenManager {
    // session 过期时间
    private static final long SESSION_TIMEOUT_MS = 30 * 60 * 1000; // 30分钟
    // session存储的token前缀
    private static final String TOKEN_ATTRIBUTE_PREFIX = "TOKEN_";
    // session存储的token时间前缀
    public static final String TOKEN_EXPIRY_PREFIX = "TOKEN_EXPIRY_";

    /**
     * 设置用户Token及其过期时间到session
     *
     * @param request HTTP请求对象
     * @param account 用户账号
     * @param token   用户令牌
     */
    public static void setAccountToken(HttpServletRequest request, String account, String token) {
        HttpSession session = request.getSession();
        session.setAttribute(TOKEN_ATTRIBUTE_PREFIX + token, account);
        long expiryTime = System.currentTimeMillis() + SESSION_TIMEOUT_MS;
        session.setAttribute(TOKEN_EXPIRY_PREFIX + token, expiryTime);
    }

    /**
     * 验证Token是否有效
     *
     * @param request HTTP请求对象
     * @return boolean 是否有效
     */
    public static boolean isTokenValid(HttpServletRequest request) {
        String token = getTokenFromCookies(request);
        if (token == null) {
            return false;
        }
        //String account = getAccountFromCookies(request);
        //if (account == null) {
        //    return false;
        //}
        // 验证token 和 账户是否绑定
        //String accountFromSession = getAccountFromSession(request);
        //if (!StringUtils.equals(account, accountFromSession)) {
        //    throw new NotLoggedInException("Token not found in cookies");
        //}

        HttpSession session = request.getSession(false);
        if (session != null) {
            Long expiryTime = (Long) session.getAttribute(TOKEN_EXPIRY_PREFIX + token);
            return expiryTime != null && expiryTime >= System.currentTimeMillis();
        }
        return false;
    }

    /**
     * 刷新Token的过期时间
     *
     * @param request HTTP请求对象
     */
    public static void onSuccessfulRequest(HttpServletRequest request) {
        String account = getAccountFromCookies(request);
        if (account == null) {
            throw new NotLoggedInException("Token not found in cookies");
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            String token = getTokenFromCookies(request);
            if (token != null) {
                long newExpiryTime = System.currentTimeMillis() + SESSION_TIMEOUT_MS;
                session.setAttribute(TOKEN_EXPIRY_PREFIX + token, newExpiryTime);
            }
        }
    }

    /**
     * 从Cookie中获取用户账号
     *
     * @param request HTTP请求对象
     * @return String 用户账号
     */
    private static String getAccountFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CommonConstant.X_USER_ACCOUNT)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 从Cookie中获取Token
     *
     * @param request HTTP请求对象
     * @return String 用户令牌
     */
    private static String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CommonConstant.X_Token)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 从session中获取用户账号
     *
     * @param request HTTP请求对象
     * @return String 用户账号
     */
    public static String getAccountFromSession(HttpServletRequest request) {
        String token = getTokenFromCookies(request);
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(TOKEN_ATTRIBUTE_PREFIX + token);
        }
        return null;
    }

}
