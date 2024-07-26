package com.wangshuos.qrcodepay.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName Order
 * @Author wangshuo
 * @Date 2024/7/24 14:39
 * @Version 1.0
 **/
@Data
public class Order {
    @Schema(description = "订单ID")
    private String orderId;
    @Schema(description = "产品ID")
    private String productId;
    @Schema(description = "产品名称")
    private String productName;
    @Schema(description = "产品价格")
    private String productPrice;
    @Schema(description = "产品数量")
    private String productQuantity;
}
