package com.wangshuos.websocket.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wangshuos.websocket.entity.ChatFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/26 14:35:46
 */
public interface ChatFileService extends IService<ChatFile> {

    String addChatFile(ChatFile chatFile);

}
