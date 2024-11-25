package com.wangshuos.kafka.producer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wangshuos.kafka.producer.request.WebsocketChat;
import com.wangshuos.kafka.producer.service.ChatProducerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChatProducetServer
 * @Author wangshuo
 * @Date 2024/8/23 09:47
 * @Version 1.0
 **/
@Service
@Log4j2
public class ChatProducerServiceImpl implements ChatProducerService {
    private static final String TOPIC_CHAT = "acceptUserIdAndMessage";
    private static final String TOPIC_USER = "user-sign-in";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(WebsocketChat websocketChat) {
        log.error("ChatProducer send message: {}", websocketChat.toString());
        try {
            kafkaTemplate.send(TOPIC_CHAT, String.valueOf(websocketChat.getAcceptUserId()), new JsonMapper().writeValueAsString(websocketChat));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendUserSignIn(String userId) {
        kafkaTemplate.send(TOPIC_USER, userId);
    }

}
