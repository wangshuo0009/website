package com.wangshuos.qrcodepay.controller;

import com.alipay.api.AlipayApiException;
import com.wangshuos.qrcodepay.request.OrderRequest;
import com.wangshuos.qrcodepay.service.AlipayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName PayController
 * @Author wangshuo
 * @Date 2024/7/24 15:06
 * @Version 1.0
 **/
@RestController
@RequestMapping("/pay")
@Tag(name = "支付")
public class PayController {

    @Autowired
    private AlipayService alipayService;

    @PostMapping("/alipay")
    public String alipay(@RequestBody OrderRequest orderRequest) throws AlipayApiException {
        return alipayService.getQrCode(orderRequest);
    }
    @Operation(summary = "支付测试")
    @GetMapping("/alipay")
    public String alipay() throws AlipayApiException {
        return alipayService.getQrCode();
    }

    @PostMapping("/generateQRCode")
    public String generateQRCode(@RequestBody OrderRequest orderRequest) throws AlipayApiException {
        return alipayService.generateQRCodeBase64(orderRequest);
    }
}
