package com.wangshuos.kafka.producer.controller;

import com.wangshuos.kafka.producer.request.WebsocketChat;
import com.wangshuos.kafka.producer.service.ChatProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ChatProducerController
 * @Author wangshuo
 * @Date 2024/8/23 12:51
 * @Version 1.0
 **/
@RestController
@RequestMapping("/kafka")
public class ChatProducerController {

    @Autowired
    private ChatProducerService chatProducerService;

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody WebsocketChat websocketChat) {
        chatProducerService.sendMessage(websocketChat);
    }

    @GetMapping("/sendUserSignIn/{userId}")
    public void sendUserSignIn(@PathVariable String userId) {
        chatProducerService.sendUserSignIn(userId);
    }



}
