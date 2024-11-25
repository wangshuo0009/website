//package com.wangshuos.kafka.consumer.controller;
//
//import com.wangshuos.kafka.consumer.service.WebSocketChatConsumerService;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @ClassName ChatConsumerController
// * @Author wangshuo
// * @Date 2024/8/23 12:51
// * @Version 1.0
// **/
//@RestController
//@RequestMapping("/kafka")
//public class ChatConsumerController {
//    @Autowired
//    private WebSocketChatConsumerService chatConsumerService;
//
//    @PostMapping("/chatConsumer")
//    public void chatConsumer(ConsumerRecord<String, String> record) {
//        chatConsumerService.listen(record);
//    }
//
//
//
//}
