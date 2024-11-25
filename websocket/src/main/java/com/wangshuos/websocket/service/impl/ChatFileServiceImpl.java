package com.wangshuos.websocket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangshuos.websocket.entity.ChatFile;
import com.wangshuos.websocket.mapper.ChatFileMapper;
import com.wangshuos.websocket.service.ChatFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/26 14:35:46
 */
@Service
public class ChatFileServiceImpl extends ServiceImpl<ChatFileMapper, ChatFile> implements ChatFileService {

    @Autowired
    private ChatFileMapper chatFileMapper;
    @Override
    public String addChatFile(ChatFile chatFile) {
        //chatFileMapper
        return "";
    }
}
