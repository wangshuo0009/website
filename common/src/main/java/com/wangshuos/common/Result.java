package com.wangshuos.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "统一返回")
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "是否成功")
    private Boolean success;
    @Schema(description = "返回状态码")
    private Integer code;
    @Schema(description = "返回信息")
    private String message;
    @Schema(description = "返回数据")
    private T data;

    public Result(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> getInstance(Boolean success, Integer code, String message, T data) {
        return new Result<>(success, code, message, data);
    }

    public static <T> Result<T> success() {
        return getInstance(true, HttpStatus.OK.value(), null, null);
    }

    public static <T> Result<T> success(String message) {
        return getInstance(true, HttpStatus.OK.value(), message, null);
    }

    public static <T> Result<T> success(String message, T data) {
        return getInstance(true, HttpStatus.OK.value(), message, data);
    }

    public static <T> Result<T> success(Integer code, String message, T data) {
        return getInstance(true, code, message, data);
    }

    public static <T> Result<T> fail() {
        return getInstance(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null);
    }

    public static <T> Result<T> fail(String message) {
        return getInstance(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    public static <T> Result<T> fail(String message, T data) {
        return getInstance(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
    }

    public static <T> Result<T> fail(Integer code, String message, T data) {
        return getInstance(false, code, message, data);
    }
}