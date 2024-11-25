package com.wangshuos.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


// Generated by https://start.springboot.io

//@ComponentScan(basePackages = {"com.wangshuos.*.*"})
//@ComponentScan(basePackages = {"com.wangshuos.redis", "com.wangshuos.websocket"})
@EnableAsync
@SpringBootApplication
//@EnableDiscoveryClient
public class WebsocketApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebsocketApplication.class, args);
	}

}
// 优质的 spring/boot/data/security/cloud 框架中文文档尽在 => https://springdoc.cn