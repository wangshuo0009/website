//package com.wangshuos.websocket.server;
//
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wangshuos.common.constant.CommonConstant;
//import com.wangshuos.websocket.entity.User;
//import com.wangshuos.websocket.entity.WebsocketChat;
//import com.wangshuos.websocket.service.UserService;
//import com.wangshuos.websocket.service.WebsocketChatService;
//import jakarta.websocket.server.ServerEndpoint;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.net.HttpCookie;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * @ClassName WebsocketChatServer
// * @Author wangshuo
// * @Date 2024/7/30 10:27
// * @Version 1.0
// **/
//@ServerEndpoint("/chat")
//@Log4j2
//@Service
//@Transactional(rollbackFor = Exception.class)
//public class WebsocketChatServer extends TextWebSocketHandler {
//    // 存储所有已连接的 WebSocket 会话
//    private static final Map<String, WebSocketSession> webSocketSessionMap = new HashMap<>();
//
//
//    // @ServerEndpoint 不是由spring管理的，所以不能直接 @Autowired
//    static UserService userService;
//    static WebsocketChatService websocketChatService;
//    static WebSocketChatSessionMangerServer webSocketChatSessionMangerServer;
//    static RestTemplate restTemplate;
//
//    static String kafkaHostPort;
//    Boolean kafkaEnable = false;
//    Boolean redisEnable = false;
//
//    @Override
//    public void afterConnectionEstablished(@NonNull WebSocketSession webSocketSession) throws Exception {
//        // 当新的 WebSocket 连接建立时调用
//        String userId = getUserIdFromSession(webSocketSession); // 从会话中获取用户标识符
//        if (StringUtils.isNotBlank(userId)) {
//            webSocketSessionMap.put(userId, webSocketSession);
//            if (redisEnable) {
//                webSocketChatSessionMangerServer.setWebSocketSessionId(userId, webSocketSession.getId());
//            }
//            // 向kafka发送谁谁谁上线了，以便推送消息
//            if (kafkaEnable) {
//                restTemplate.getForEntity(kafkaHostPort+"/kafka/sendUserSignIn/" + userId, String.class);
//            }
//            log.info("{} 已连接!", userId);
//        } else {
//            webSocketSession.close(CloseStatus.BAD_DATA); // 如果用户标识符无效，则关闭连接
//        }
//    }
//
//    @Override
//    protected void handleTextMessage(@NonNull WebSocketSession webSocketSession, TextMessage textMessage) throws Exception {
//        // 处理收到的文本消息
//        String sendUserId = getUserIdFromSession(webSocketSession); // 获取发送消息的用户标识符
//        String message = textMessage.getPayload(); // 获取消息内容
//        System.out.println("收到消息: " + message + " from " + sendUserId);
//
//        // 确定接收消息的目标用户
//        String acceptUserId = getAcceptIdFromSession(webSocketSession);
//        User user = userService.getById(acceptUserId);
//        boolean online = false;
//
//        if (ObjectUtils.isEmpty(user)) {
//            log.info("不存在用户账号");
//        }
//        if (!ObjectUtils.isEmpty(webSocketSessionMap.get(acceptUserId))) {
//            online = true;
//        }
//        sendMessageToUser(sendUserId, acceptUserId, message , online); // 将消息发送给目标用户
//    }
//
//    @Override
//    public void afterConnectionClosed(@NonNull WebSocketSession webSocketSession, @NonNull CloseStatus status) throws Exception {
//        // 当 WebSocket 连接关闭时调用
//        String userId = getUserIdFromSession(webSocketSession); // 获取关闭连接的用户标识符
//        if (StringUtils.isNotBlank(userId)) {
//            if (redisEnable) {
//                webSocketChatSessionMangerServer.removeWebSocketSessionId(userId);
//            }
//        }
//        log.info("{} 已断开", userId);
//    }
//
//    // 从 WebSocket 会话的 Cookie 获取用户id
//    private String getUserIdFromSession(WebSocketSession webSocketSession) {
//        // 获取握手时的HTTP头信息
//        HttpHeaders headers = webSocketSession.getHandshakeHeaders();
//
//        // 从HTTP头中获取Cookie信息
//        List<String> cookies = headers.get(HttpHeaders.COOKIE);
//        if (cookies != null) {
//            for (String cookie : cookies) {
//                // 解析Cookie
//                String[] cookiePairs = cookie.split(";");
//                for (String pair : cookiePairs) {
//                    List<HttpCookie> parsedCookie = HttpCookie.parse(pair);
//                    String cookieName = parsedCookie.get(0).getName();
//                    if (ObjectUtils.nullSafeEquals(cookieName, CommonConstant.X_USER_ID)){
//                        return parsedCookie.get(0).getValue();
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    // 从 WebSocket 会话的 URl 获取用户id
//    private String getAcceptIdFromSession(WebSocketSession webSocketSession) {
//        return Objects.requireNonNull(webSocketSession.getUri()).getQuery();
//    }
//
//
//    // 向特定用户发送消息
//    private void sendMessageToUser(String sendUserId, String acceptUserId, String message, boolean online) {
//        WebsocketChat websocketChat = new WebsocketChat() {{
//            setAcceptUserId(Integer.parseInt(acceptUserId));
//            setSendUserId(Integer.parseInt(sendUserId));
//            setContent(message);
//            setCreateTime(LocalDateTime.now());
//            setIsReader(online);
//        }};
//        boolean save = websocketChatService.save(websocketChat);
//        if (save) {
//            sendMessageToWebSocketSession(acceptUserId, message, websocketChat);
//        }
//    }
//
//    private void sendMessageToWebSocketSession(String acceptUserId, String message, WebsocketChat websocketChat) {
//        WebSocketSession webSocketSession = webSocketSessionMap.get(acceptUserId);
//
//        if (webSocketSession != null && webSocketSession.isOpen()) {
//            try {
//                webSocketSession.sendMessage(new TextMessage(message));
//            } catch (Exception e) {
//                log.error("发送失败: {}", e.getMessage(), e);
//            }
//        } else if (!ObjectUtils.isEmpty(websocketChat)) {
//            if (kafkaEnable) {
//                sendToKafka(websocketChat);
//            }
//        }
//    }
//
//    private void sendToKafka(WebsocketChat websocketChat) {
//        try {
//            String websocketChatJson = new ObjectMapper().writeValueAsString(websocketChat);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> entity = new HttpEntity<>(websocketChatJson, headers);
//            restTemplate.postForEntity(kafkaHostPort + "/kafka/sendMessage", entity, String.class);
//        } catch (Exception e) {
//            log.error("Failed to send message to Kafka: {}", e.getMessage(), e);
//        }
//    }
//
//    // 简化的直接发送消息方法
//    public void sendMessageToUser(String acceptUserId, String message) {
//        sendMessageToWebSocketSession(acceptUserId, message, null);
//    }
//
//
//
//
//
//    @Value("${definition.kafka}")
//    public void setRemoteUploadUrl(String kafkaHostPort) {
//        WebsocketChatServer.kafkaHostPort = kafkaHostPort;
//    }
//
//    @Autowired
//    public void setUserService(UserService userService) {
//        WebsocketChatServer.userService = userService;
//    }
//    @Autowired
//    public void setWebsocketChatService(WebsocketChatService websocketChatService) {
//        WebsocketChatServer.websocketChatService = websocketChatService;
//    }
//    @Autowired
//    public void setWebSocketChatMangerServer(WebSocketChatSessionMangerServer webSocketChatSessionMangerServer) {
//        WebsocketChatServer.webSocketChatSessionMangerServer = webSocketChatSessionMangerServer;
//    }
//    @Autowired
//    public void setRestTemplate(RestTemplate restTemplate) {
//        WebsocketChatServer.restTemplate = restTemplate;
//    }
//
//}
