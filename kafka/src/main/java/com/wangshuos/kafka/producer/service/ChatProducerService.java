package com.wangshuos.kafka.producer.service;

import com.wangshuos.kafka.producer.request.WebsocketChat;

/**
 * @ClassName ChatProducerService
 * @Author wangshuo
 * @Date 2024/8/23 12:53
 * @Version 1.0
 **/
public interface ChatProducerService {
    void sendMessage(WebsocketChat websocketChat);
    void sendUserSignIn(String userId);
}
