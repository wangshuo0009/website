package com.wangshuos.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangshuos.websocket.entity.User;
import com.wangshuos.websocket.mapper.UserMapper;
import com.wangshuos.websocket.mapper.WebsocketChatMapper;
import com.wangshuos.websocket.response.UserListResponse;
import com.wangshuos.websocket.response.WebSocketChatResponse;
import com.wangshuos.websocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/21 10:23:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebsocketChatMapper websocketChatMapper;

    public List<UserListResponse> searchUserList(Integer signInUserId) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>() {{
            ne(User::getId, signInUserId);
            select(User::getId, User::getName, User::getAccount);
        }};

        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        List<WebSocketChatResponse> webSocketChatResponses = websocketChatMapper.selectLastChat(signInUserId);
        // 创建一个 Map 来存储用户 ID 与最新聊天记录的映射
        Map<Integer, WebSocketChatResponse> chatResponseMap = webSocketChatResponses.stream()
                .filter(chat -> Objects.nonNull(chat.getAccount()))  // 过滤掉 account 为 null 的情况
                .collect(Collectors.toMap(
                        WebSocketChatResponse::getAccount,
                        chat -> chat
                ));

        // 将用户信息和聊天记录结合
        return users.stream().map(user -> {
                    UserListResponse response = new UserListResponse();
                    response.setId(user.getId());
                    response.setName(user.getName());
                    response.setAccount(user.getAccount());
                    // 确保 account 不为 null
                    if (Objects.nonNull(user.getId())) {
                        WebSocketChatResponse chatResponse = chatResponseMap.get(user.getId());
                        if (chatResponse != null) {
                            response.setLastMessage(chatResponse.getContent());
                            response.setLastMessageTime(chatResponse.getCreateTime());
                            response.setUnreadMessageCount(chatResponse.getUnreadMessageCount());
                            response.setFileId(chatResponse.getFileId());
                        }
                    }
                    return response;
                })
                .sorted(Comparator.comparing(UserListResponse::getLastMessageTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }
}
