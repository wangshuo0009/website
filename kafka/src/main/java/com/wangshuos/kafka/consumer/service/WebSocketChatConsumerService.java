package com.wangshuos.kafka.consumer.service;

import com.wangshuos.kafka.producer.request.WebsocketChat;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @ClassName ChatConsumerService
 * @Author wangshuo
 * @Date 2024/8/23 12:52
 * @Version 1.0
 **/
public interface WebSocketChatConsumerService {
    void listen(ConsumerRecord<String, WebsocketChat> record);
}
