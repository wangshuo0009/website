package com.wangshuos.common.exception;

/**
 * @ClassName NotLoggedInException
 * @Author wangshuo
 * @Date 2024/5/10 17:26
 * @Version 1.0
 **/
public class NotLoggedInException extends RuntimeException {
    public NotLoggedInException() {
        super("用户未登录");
    }

    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}
