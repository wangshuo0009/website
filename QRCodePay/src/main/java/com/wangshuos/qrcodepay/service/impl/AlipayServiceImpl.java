package com.wangshuos.qrcodepay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.file.ByteArrayOutputStream;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.wangshuos.qrcodepay.config.MyAlipayConfig;
import com.wangshuos.qrcodepay.request.OrderRequest;
import com.wangshuos.qrcodepay.service.AlipayService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AlipayServiceImpl
 * @Author wangshuo
 * @Date 2024/7/24 13:46
 * @Version 1.0
 **/
@Service
@Log4j2
public class AlipayServiceImpl implements AlipayService {
    private static final OrderRequest orderRequest = new OrderRequest(){{
        setOrderId("wangshuo-orderId-01");
        setProductId("wangshuo-productId");
        setProductName("商品名称");
        setProductPrice(new BigDecimal("0.01"));
        setProductQuantity(1);

    }};

    @Override
    public String getQrCode(OrderRequest orderRequest) {
        String orderId = orderRequest.getOrderId();
        String productId = orderRequest.getProductId();
        String productName = orderRequest.getProductName();
        BigDecimal productPrice = orderRequest.getProductPrice();
        Integer productQuantity = orderRequest.getProductQuantity();

        // 计算总价
        BigDecimal totalPrice = productPrice.multiply(new BigDecimal(productQuantity));

        try {
            MyAlipayConfig myAlipayConfig = new MyAlipayConfig();
            // 创建支付宝支付对象
            AlipayClient alipayClient = new DefaultAlipayClient(myAlipayConfig.getAlipayConfig());

            // 构造支付宝支付请求参数
            AlipayTradePrecreateRequest aliPayRequest = new AlipayTradePrecreateRequest();
            AlipayTradePrecreateModel aliPayModel = new AlipayTradePrecreateModel();
            aliPayModel.setOutTradeNo(orderId);
            aliPayModel.setSubject(productName + ": ProductId" + productId + "; Order: " + orderId);
            aliPayModel.setTotalAmount(String.valueOf(totalPrice));
            aliPayRequest.setBizModel(aliPayModel);

            // 发起支付宝支付预下单请求
            AlipayTradePrecreateResponse aliPayResponse = alipayClient.execute(aliPayRequest);
            if (aliPayResponse.isSuccess()) {
                String qrCodeUrl = aliPayResponse.getQrCode();
                log.error("下单成功");

                // 返回合一支付二维码URL
                return qrCodeUrl;
            } else {
                log.error("下单失败");
                // 支付宝支付预下单失败
                throw new AlipayApiException();
                //return null;
            }

        } catch (AlipayApiException e) {
            // 定义资源
            // 获取当前类的 ClassLoader
            //ClassLoader classLoader = ClassLoader.class.getClassLoader();
            //
            //// 资源路径相对于 resources 文件夹
            //URL resource = classLoader.getResource("images/example.png");
            //Resource resource = new ClassPathResource("./images/加载失败了.png");
            return generateQRCode("失败了");
            //
            //try {
            //    //String s = encodeImageToBase64(resource.getURL().getPath());
            //} catch (IOException ex) {
            //    throw new RuntimeException(ex);
            //}
        }
    }

    //@Override
    //public String getQrCode() throws AlipayApiException {
    //
    //    orderRequest = orderRequest;
    //
    //    try {
    //        // 创建支付宝支付对象
    //        AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig());
    //
    //        // 构造支付宝支付请求参数
    //        AlipayTradePrecreateRequest aliPayRequest = new AlipayTradePrecreateRequest();
    //        AlipayTradePrecreateModel aliPayModel = new AlipayTradePrecreateModel();
    //        aliPayModel.setOutTradeNo("orderId");
    //        aliPayModel.setSubject("productId ");
    //        aliPayModel.setTotalAmount(String.valueOf(11));
    //        aliPayRequest.setBizModel(aliPayModel);
    //
    //        // 发起支付宝支付预下单请求
    //        AlipayTradePrecreateResponse aliPayResponse = alipayClient.execute(aliPayRequest);
    //        if (aliPayResponse.isSuccess()) {
    //            String qrCodeUrl = aliPayResponse.getQrCode();
    //            log.error("下单成功");
    //
    //            // 返回合一支付二维码URL
    //            return qrCodeUrl;
    //        } else {
    //            log.error("下单失败");
    //            // 支付宝支付预下单失败
    //            throw new AlipayApiException();
    //            //return null;
    //        }
    //
    //    } catch (AlipayApiException e) {
    //        e.printStackTrace();
    //        return generateQRCode("失败了");
    //    }
    //}
    @Override
    public String getQrCode() throws AlipayApiException {

        String orderId = orderRequest.getOrderId();
        String productId = orderRequest.getProductId();
        String productName = orderRequest.getProductName();
        BigDecimal productPrice = orderRequest.getProductPrice();
        Integer productQuantity = orderRequest.getProductQuantity();

        // 计算总价
        BigDecimal totalPrice = productPrice.multiply(new BigDecimal(productQuantity));

        try {
            MyAlipayConfig myAlipayConfig = new MyAlipayConfig();
            // 创建支付宝支付对象
            AlipayClient alipayClient = new DefaultAlipayClient(myAlipayConfig.getAlipayConfig());

            // 构造支付宝支付请求参数
            AlipayTradePrecreateRequest aliPayRequest = new AlipayTradePrecreateRequest();
            AlipayTradePrecreateModel aliPayModel = new AlipayTradePrecreateModel();
            aliPayModel.setOutTradeNo(orderId);
            aliPayModel.setSubject(productName + ": ProductId" + productId + "; Order: " + orderId);
            aliPayModel.setTotalAmount(String.valueOf(totalPrice));
            aliPayRequest.setBizModel(aliPayModel);

            // 发起支付宝支付预下单请求
            AlipayTradePrecreateResponse aliPayResponse = alipayClient.execute(aliPayRequest);
            if (aliPayResponse.isSuccess()) {
                String qrCodeUrl = aliPayResponse.getQrCode();
                log.info("下单成功");

                // 返回合一支付二维码URL
                return generateQRCode(qrCodeUrl);
            } else {
                log.info("下单失败");
                // 支付宝支付预下单失败
                throw new AlipayApiException();
                //return null;
            }
        } catch (AlipayApiException e) {
            return generateQRCode("失败了");
        }
    }


    @Override
    public String generateQRCodeBase64(OrderRequest orderRequest) {
        String qrCode = getQrCode(orderRequest);
        return generateQRCode(qrCode);
    }

    public String generateQRCode(String qrCode) {

        int width = 300; // 图像宽度
        int height = 300; // 图像高度
        String format = "png"; // 图像格式

        // 设置二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            // 生成二维码
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCode, BarcodeFormat.QR_CODE, width, height, hints);

            // 将 BitMatrix 转换为 BufferedImage
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // 将 BufferedImage 转换为 Base64 字符串
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, format, byteArrayOutputStream);
            byte[] pngData = byteArrayOutputStream.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeImageToBase64(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

}
