package com.wangshuos.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangshuos.websocket.entity.WebsocketChat;
import com.wangshuos.websocket.mapper.WebsocketChatMapper;
import com.wangshuos.websocket.service.WebsocketChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/21 10:23:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class WebsocketChatServiceImpl extends ServiceImpl<WebsocketChatMapper, WebsocketChat> implements WebsocketChatService {
    @Autowired
    private WebsocketChatMapper websocketChatMapper;

    @Async
    @Override
    public void updateWebsocketChatIsReader(List<WebsocketChat> websocketChatList, Integer searchUserAccount) {
        AtomicBoolean update = new AtomicBoolean(false);
        List<Integer> chatIds = new ArrayList<>();
        websocketChatList.forEach(websocketChat -> {
            if (!websocketChat.getIsReader() && ObjectUtils.nullSafeEquals(searchUserAccount, websocketChat.getAcceptUserId())){
                update.set(true);
                chatIds.add(websocketChat.getId());
            }
        });
        if (update.get()){
            websocketChatMapper.update(new LambdaUpdateWrapper<>(){{
                in(WebsocketChat::getId, chatIds);
                set(WebsocketChat::getIsReader, true);
            }});
        }
    }

    //@Override
    //public void sendMessageForSessionId(String acceptUserId, List<WebsocketChatRequest> websocketChatRequestList) {
    //    try {
    //        ObjectMapper objectMapper = new ObjectMapper();
    //        String s = objectMapper.writeValueAsString(websocketChatRequestList);
    //        websocketChatServer.sendMessageToUser(acceptUserId, s);
    //    } catch (JsonProcessingException e) {
    //        e.printStackTrace();
    //    }
    //
    //}
}
