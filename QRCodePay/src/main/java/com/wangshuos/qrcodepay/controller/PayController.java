package com.wangshuos.qrcodepay.controller;

import com.alipay.api.AlipayApiException;
import com.wangshuos.common.Result;
import com.wangshuos.qrcodepay.request.OrderRequest;
import com.wangshuos.qrcodepay.service.AlipayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PayController
 * @Author wangshuo
 * @Date 2024/7/24 15:06
 * @Version 1.0
 **/
@RestController
@RequestMapping("/pay")
@Tag(name = "支付")
@Log4j2
public class PayController {
    @Autowired
    private AlipayService alipayService;

    @PostMapping("/alipay")
    public Result<?> alipay(@RequestBody OrderRequest orderRequest) {
        try {
            return Result.success("下单成功请支付",alipayService.getQrCode(orderRequest));
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Result.fail("订单请求异常" + e.getMessage());
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return Result.fail("订单金额计算异常" + e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Result.fail("数据转换异常" + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Result.fail("非法金额" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("服务器内部异常" + e.getMessage());
        }
    }
    //@Operation(summary = "支付测试")
    //@GetMapping("/alipay")
    //public String alipay() throws AlipayApiException {
    //    return alipayService.getQrCode();
    //}

    @PostMapping("/generateQRCode")
    public String generateQRCode(@RequestBody OrderRequest orderRequest) throws AlipayApiException {
        return alipayService.generateQRCodeBase64(orderRequest);
    }
}
