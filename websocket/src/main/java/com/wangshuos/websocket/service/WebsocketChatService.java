package com.wangshuos.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangshuos.websocket.entity.WebsocketChat;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/21 10:23:08
 */
public interface WebsocketChatService extends IService<WebsocketChat> {

    void updateWebsocketChatIsReader(List<WebsocketChat> websocketChatList, Integer searchUserAccount);

    //void sendMessageForSessionId(String acceptUserId, List<WebsocketChatRequest> websocketChatRequestList);


}
