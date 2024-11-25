package com.wangshuos.redis.server;

import com.wangshuos.common.constant.CommonConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author wangshuo
 * @Description Token管理类，用于管理用户的Token及其有效期
 * @Date 2024/5/9 14:07
 **/
@Service
public class TokenManagerServer {
    public static final String TOKEN_PREFIX = "TOKEN_";
    public static final int TOKEN_TIMEOUT = 30;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置用户Token及其过期时间
     *
     * @param account 用户账号
     * @param token   用户令牌
     */
    public void setAccountToken(String account, String token) {
        String s = stringRedisTemplate.opsForValue().get(TOKEN_PREFIX + account);
        if (StringUtils.isNotBlank(s)) {
            stringRedisTemplate.delete(TOKEN_PREFIX + account);
        }
        stringRedisTemplate.opsForValue().set(TOKEN_PREFIX + account, token, TOKEN_TIMEOUT, TimeUnit.MINUTES);
    }

    /**
     * 验证Token是否有效
     *
     * @param request HTTP请求对象
     * @return boolean 是否有效 有效直接刷新时间
     */
    public boolean isTokenValid(HttpServletRequest request) {
        Map<String, String> cookiesMap = getTokenFromCookies(request);
        if (cookiesMap.isEmpty()) {
            return false;
        }
        String cookiesToken = cookiesMap.get(CommonConstant.X_Token);
        String cookiesAccount = cookiesMap.get(CommonConstant.X_USER_ACCOUNT);

        String redisToken = stringRedisTemplate.opsForValue().get(TOKEN_PREFIX + cookiesAccount);
        if (StringUtils.isNotBlank(redisToken)) {
            stringRedisTemplate.expire(TOKEN_PREFIX + cookiesAccount, TOKEN_TIMEOUT, TimeUnit.MINUTES);
        }
        return StringUtils.isNotBlank(redisToken);
    }

    /**
     * 刷新Token的过期时间
     *
     * @param request HTTP请求对象
     */
    public void onSuccessfulRequest(HttpServletRequest request) {
        Map<String, String> cookiesMap = getTokenFromCookies(request);
        if (cookiesMap.isEmpty()) {
            return;
        }
        String cookiesToken = cookiesMap.get(CommonConstant.X_Token);
        String account = stringRedisTemplate.opsForValue().get(TOKEN_PREFIX + cookiesToken);

        if (StringUtils.isNotBlank(account)) {
            stringRedisTemplate.expire(TOKEN_PREFIX + cookiesToken, TOKEN_TIMEOUT, TimeUnit.MINUTES);
        }
    }


    /**
     * 从Cookie中获取信息
     *
     * @param request HTTP请求对象
     * @return Map cookiesMap
     */
    private Map<String, String> getTokenFromCookies(HttpServletRequest request) {
        Map<String, String> cookiesMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookiesMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookiesMap;
    }


}
