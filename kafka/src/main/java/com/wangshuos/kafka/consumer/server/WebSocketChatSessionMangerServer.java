package com.wangshuos.kafka.consumer.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * @ClassName WebSocketChatMangerServer
 * @Author wangshuo
 * @Date 2024/8/23 10:32
 * @Version 1.0
 **/
@Service
public class WebSocketChatSessionMangerServer {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final String CHAT_PREFIX = "CHAT_";

    public void setWebSocketSessionId(@NonNull String userId, @NonNull String webSocketSessionId) {
        stringRedisTemplate.opsForValue().set(CHAT_PREFIX + userId, webSocketSessionId);
    }

    public String getWebSocketSessionId(@NonNull String userId) {
        return stringRedisTemplate.opsForValue().get(CHAT_PREFIX + userId);
    }

    public void removeWebSocketSessionId(@NonNull String userId) {
        stringRedisTemplate.delete(CHAT_PREFIX + userId);
    }


}
