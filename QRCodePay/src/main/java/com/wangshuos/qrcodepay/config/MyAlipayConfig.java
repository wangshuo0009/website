package com.wangshuos.qrcodepay.config;

import com.alipay.api.AlipayConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @ClassName AlipayConfig
 * @Author wangshuo
 * @Date 2024/7/24 14:17
 * @Version 1.0
 **/
@Log4j2
@Component
public class MyAlipayConfig {
    @Value("${alipay.appID}")
    private String APP_ID;
    @Value("${alipay.appPrivateKeyUrl}")
    private String APP_PRIVATE_KEY_URL;
    @Value("${alipay.alipayPublicKeyUrl}")
    private String ALIPAY_PUBLIC_KEY_URL;
    @Value("${alipay.serverUrl}")
    private String SERVER_URL;
    private static final String CHARSET = "UTF-8";
    private static final String FORMAT = "json";
    private static final String SIGN_TYPE = "RSA2";

    public AlipayConfig getAlipayConfig() {
        com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
        alipayConfig.setServerUrl(SERVER_URL);
        alipayConfig.setAppId(APP_ID);
        alipayConfig.setPrivateKey(readKey(APP_PRIVATE_KEY_URL));
        alipayConfig.setFormat(FORMAT);
        alipayConfig.setAlipayPublicKey(readKey(ALIPAY_PUBLIC_KEY_URL));
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(SIGN_TYPE);
        return alipayConfig;
    }

    private String readKey(String filePath) {
        try {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            }
            String text = content.toString().trim();
            if (StringUtils.isBlank(text)){
                throw new IOException("未获取密钥");
            }
            log.info("文件读取成功：" + text);
            return text;
        } catch (IOException e) {
            log.error("文件读取失败：" + filePath, e);
            throw new RuntimeException(e);
        }
    }
}