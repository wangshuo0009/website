package com.wangshuos.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "统一返回状态码常量")
public interface ResultCodeConst {

    @Schema(description = "成功")
    Integer SUCCESS = 200;
    @Schema(description = "失败")
    Integer FAIL = 500;
}