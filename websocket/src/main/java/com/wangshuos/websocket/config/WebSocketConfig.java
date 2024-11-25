package com.wangshuos.websocket.config;

import com.wangshuos.websocket.server.WebsocketNotRedisNacosKafkaServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket // 启用 WebSocket 功能
public class WebSocketConfig implements WebSocketConfigurer {

    //@Override
    //public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    //    // 注册 WebSocket 处理器 ChatHandler，映射到 /chat 端点
    //    registry.addHandler(new WebsocketChatServer(), "/chat")
    //            .setAllowedOrigins("*"); // 允许所有来源连接
    //}

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 WebSocket 处理器 ChatHandler，映射到 /chat 端点
        registry.addHandler(new WebsocketNotRedisNacosKafkaServer(), "/chat")
                .setAllowedOrigins("*"); // 允许所有来源连接
    }

}