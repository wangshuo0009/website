package com.wangshuos.websocket.server;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wangshuos.common.constant.CommonConstant;
import com.wangshuos.websocket.entity.ChatFile;
import com.wangshuos.websocket.entity.User;
import com.wangshuos.websocket.entity.WebsocketChat;
import com.wangshuos.websocket.service.ChatFileService;
import com.wangshuos.websocket.service.UserService;
import com.wangshuos.websocket.service.WebsocketChatService;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.File;
import java.net.HttpCookie;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.wangshuos.common.util.FileUtils.fileToBase64;

/**
 * @ClassName WebsocketNotRedisNacosKafkaServer
 * @Author wangshuo
 * @Date 2024/8/24 19:56
 * @Version 1.0
 **/
@ServerEndpoint("/chat")
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class WebsocketNotRedisNacosKafkaServer extends TextWebSocketHandler {
    // 存储所有已连接的 WebSocket 会话
    private static final Map<String, WebSocketSession> webSocketSessionMap = new HashMap<>();

    // @ServerEndpoint 不是由spring管理的，所以不能直接 @Autowired
    static UserService userService;
    static WebsocketChatService websocketChatService;
    static ChatFileService chatFileService;



    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession webSocketSession) throws Exception {
        // 当新的 WebSocket 连接建立时调用
        String userId = getUserIdFromSession(webSocketSession); // 从会话中获取用户标识符


        WebSocketSession existingSession = webSocketSessionMap.get(userId);
        // 检查是否已有会话存在
        if (existingSession != null && existingSession.isOpen()) {
            // 发送关闭通知给旧的连接
            TextMessage closeNotification = new TextMessage("您的连接已在另一台设备上登录，当前连接将关闭。");
            existingSession.sendMessage(closeNotification);
            existingSession.close(CloseStatus.NORMAL);
            log.info("{} 的旧连接已关闭", userId);
        }


        // 存储新的会话
        if (StringUtils.isNotBlank(userId)) {
            webSocketSessionMap.put(userId, webSocketSession);
            webSocketSession.sendMessage(new TextMessage("登录成功"));
            log.info("{} 已连接!", userId);
        } else {
            webSocketSession.close(CloseStatus.BAD_DATA); // 如果用户标识符无效，则关闭连接
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession webSocketSession, TextMessage textMessage) throws Exception {
        // 处理收到的文本消息
        String sendUserId = getUserIdFromSession(webSocketSession); // 获取发送消息的用户标识符
        String message = textMessage.getPayload(); // 获取消息内容
        System.out.println("收到消息: " + message + " from " + sendUserId);

        // 确定接收消息的目标用户
        String acceptUserId = getAcceptIdFromSession(webSocketSession);
        User user = userService.getById(acceptUserId);
        if (ObjectUtils.isEmpty(user)) {
            log.info("不存在用户账号");
        }
        WebsocketChat websocketChat = new WebsocketChat() {{
            setAcceptUserId(Integer.parseInt(acceptUserId));
            setSendUserId(Integer.parseInt(sendUserId));
            setContent(message);
            setCreateTime(LocalDateTime.now());
            setIsReader(false);
        }};
        sendMessageToUser(websocketChat); // 将消息发送给目标用户
    }

    @Override
    protected void handlePongMessage(@NonNull WebSocketSession webSocketSession, @NonNull PongMessage pongMessage) throws Exception {
        super.handlePongMessage(webSocketSession, pongMessage);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession webSocketSession, @NonNull CloseStatus status) throws Exception {
        // 当 WebSocket 连接关闭时调用
        String userId = getUserIdFromSession(webSocketSession); // 获取关闭连接的用户标识符
        // 从 map 中移除关闭的连接
        webSocketSessionMap.remove(userId);
        log.info("{} 已断开", userId);
        log.info("{} 已断开", userId);
    }

    // 从 WebSocket 会话的 Cookie 获取用户id
    private String getUserIdFromSession(WebSocketSession webSocketSession) {
        // 获取握手时的HTTP头信息
        HttpHeaders headers = webSocketSession.getHandshakeHeaders();

        // 从HTTP头中获取Cookie信息
        List<String> cookies = headers.get(HttpHeaders.COOKIE);
        if (cookies != null) {
            for (String cookie : cookies) {
                // 解析Cookie
                String[] cookiePairs = cookie.split(";");
                for (String pair : cookiePairs) {
                    List<HttpCookie> parsedCookie = HttpCookie.parse(pair);
                    String cookieName = parsedCookie.get(0).getName();
                    if (ObjectUtils.nullSafeEquals(cookieName, CommonConstant.X_USER_ID)){
                        return parsedCookie.get(0).getValue();
                    }
                }
            }
        }
        return null;
    }

    // 从 WebSocket 会话的 URl 获取用户id
    private String getAcceptIdFromSession(WebSocketSession webSocketSession) {
        return Objects.requireNonNull(webSocketSession.getUri()).getQuery();
    }


    // 向特定用户发送消息
    public void sendMessageToUser(WebsocketChat websocketChat) {
        boolean save = websocketChatService.save(websocketChat);
        if (save) {
            String message;
            if (!ObjectUtils.isEmpty(websocketChat.getFileId())){
                ChatFile byId = chatFileService.getById(websocketChat.getFileId());
                message = fileToBase64(new File(byId.getPath()));
            } else {
                message = websocketChat.getContent();
            }
            sendMessageToWebSocketSession(websocketChat.getAcceptUserId().toString(), message, websocketChat);
        }
    }

    private void sendMessageToWebSocketSession(String acceptUserId, String message, WebsocketChat websocketChat) {
        WebSocketSession webSocketSession = webSocketSessionMap.get(acceptUserId);

        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error("发送失败: {}", e.getMessage(), e);
            }
        } else if (!ObjectUtils.isEmpty(websocketChat)) {
        }
    }
    @Autowired
    public void setUserService(UserService userService) {
        WebsocketNotRedisNacosKafkaServer.userService = userService;
    }
    @Autowired
    public void setWebsocketChatService(WebsocketChatService websocketChatService) {
        WebsocketNotRedisNacosKafkaServer.websocketChatService = websocketChatService;
    }
    @Autowired
    public void setChatFileService(ChatFileService chatFileService) {
        WebsocketNotRedisNacosKafkaServer.chatFileService = chatFileService;
    }
}
