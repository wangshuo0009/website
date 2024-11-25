package com.wangshuos.kafka.consumer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangshuos.kafka.consumer.service.UserSignInConsumerService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @ClassName ChatConsumerServer
 * @Author wangshuo
 * @Date 2024/8/23 09:48
 * @Version 1.0
 **/
@Service
@Log4j2
public class UserSignInConsumerServiceImpl implements UserSignInConsumerService {
    private static final String TOPIC_USER = "user-sign-in";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String CHAT_PREFIX = "CHAT_";

    private static final String TOPIC_CHAT = "acceptUserIdAndMessage";


    @Autowired
    private RestTemplate restTemplate;

    //@Autowired
    //public void setRestTemplate(RestTemplate restTemplate) {
    //    UserSignInConsumerServiceImpl.restTemplate = restTemplate;
    //}


    @Value("${definition.websocket}")
    private String websocketHostPost;
    @Override
    @KafkaListener(topics = TOPIC_USER, groupId = "websocket-group")
    public void listen(ConsumerRecord<String, String> record) {
        String userId = record.value();

        String webSocketSessionId = stringRedisTemplate.opsForValue().get(CHAT_PREFIX + userId);
        Boolean b = redisTemplate.hasKey(TOPIC_CHAT + userId);
        if (StringUtils.isNotBlank(webSocketSessionId) && !Boolean.FALSE.equals(b)) {
            try {
                List<Object> range = redisTemplate.opsForList().range(TOPIC_CHAT + userId, 0, -1);
                if (range.isEmpty()) {
                    return;
                }

                // 将websocketChat对象转换为JSON字符串
                ObjectMapper objectMapper = new ObjectMapper();
                String websocketChatJson = objectMapper.writeValueAsString(range);

                // 使用RestTemplate异步调用Kafka接口发送消息
                // 使用POST请求，携带消息体
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("acceptUserId", userId);
                HttpEntity<String> entity = new HttpEntity<>(websocketChatJson, headers);
                log.error(websocketHostPost + "/chatMessage/sendMessageForSessionId/"+userId);
                restTemplate.postForEntity(websocketHostPost + "/chatMessage/sendMessageForAcceptUserId", entity, String.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}
