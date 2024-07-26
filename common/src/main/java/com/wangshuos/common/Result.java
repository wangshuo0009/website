package com.wangshuos.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "统一返回")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "是否成功")
    private Boolean success;
    @Schema(description = "返回状态码")
    private Integer code;
    @Schema(description = "返回信息")
    private String message;
    @Schema(description = "返回数据")
    private T data;
    @Schema(description = "token验证")
    private String token;

    public Result(Boolean success, Integer code, String message, T data, String token) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public Result(Boolean success, Integer code, String message, T data) {
        this(success, code, message, data, null);
    }

    public static <T> Result<T> getInstance(Boolean success, Integer code, String message, T data, String token) {
        return new Result<>(success, code, message, data, token);
    }

    public static <T> Result<T> success() {
        return getInstance(true, ResultCodeConst.SUCCESS, null, null, null);
    }

    public static <T> Result<T> success(String message) {
        return getInstance(true, ResultCodeConst.SUCCESS, message, null, null);
    }

    public static <T> Result<T> success(String message, T data) {
        return getInstance(true, ResultCodeConst.SUCCESS, message, data, null);
    }

    public static <T> Result<T> success(Integer code, String message, T data, String token) {
        return getInstance(true, code, message, data, token);
    }

    public static <T> Result<T> fail() {
        return getInstance(false, ResultCodeConst.FAIL, null, null, null);
    }

    public static <T> Result<T> fail(String message) {
        return getInstance(false, ResultCodeConst.FAIL, message, null, null);
    }

    public static <T> Result<T> fail(String message, T data) {
        return getInstance(false, ResultCodeConst.FAIL, message, data, null);
    }

    public static <T> Result<T> fail(Integer code, String message, T data, String token) {
        return getInstance(false, code, message, data, token);
    }
}
