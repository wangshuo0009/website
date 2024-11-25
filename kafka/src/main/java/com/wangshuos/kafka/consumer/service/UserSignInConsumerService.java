package com.wangshuos.kafka.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @ClassName ChatConsumerService
 * @Author wangshuo
 * @Date 2024/8/23 12:52
 * @Version 1.0
 **/
public interface UserSignInConsumerService {
    void listen(ConsumerRecord<String, String> record);
}
