package com.wangshuos.kafka.consumer.service.impl;

import com.wangshuos.kafka.consumer.service.WebSocketChatConsumerService;
import com.wangshuos.kafka.producer.request.WebsocketChat;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChatConsumerServer
 * @Author wangshuo
 * @Date 2024/8/23 09:48
 * @Version 1.0
 **/
@Service
@Log4j2
public class WebSocketChatConsumerServiceImpl implements WebSocketChatConsumerService {
    private static final String TOPIC_CHAT = "acceptUserIdAndMessage";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @KafkaListener(topics = TOPIC_CHAT, groupId = "websocket-group")
    public void listen(ConsumerRecord<String, WebsocketChat> record) {
        redisTemplate.opsForList().rightPush(TOPIC_CHAT + record.key(), record.value());

    }



}
