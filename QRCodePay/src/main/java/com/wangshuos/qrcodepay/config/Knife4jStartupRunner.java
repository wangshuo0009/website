package com.wangshuos.qrcodepay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Knife4jStartupRunner implements ApplicationRunner {

    @Value("${server.port}")
    private int port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String docUrl = "http://localhost:" + port + "/doc.html";
        System.out.println("\n\nKnife4j documentation available at: " + docUrl);
    }
}
