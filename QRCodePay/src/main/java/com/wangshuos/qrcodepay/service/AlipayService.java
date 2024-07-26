package com.wangshuos.qrcodepay.service;

import com.alipay.api.AlipayApiException;
import com.wangshuos.qrcodepay.request.OrderRequest;

/**
 * @ClassName AlipayService
 * @Author wangshuo
 * @Date 2024/7/24 13:45
 * @Version 1.0
 **/
public interface AlipayService {
    public String getQrCode(OrderRequest orderRequest) throws AlipayApiException;
    public String getQrCode() throws AlipayApiException;
    public String generateQRCodeBase64(OrderRequest orderRequest) throws AlipayApiException;
}
