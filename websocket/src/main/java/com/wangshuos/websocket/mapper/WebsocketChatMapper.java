package com.wangshuos.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangshuos.websocket.entity.WebsocketChat;
import com.wangshuos.websocket.response.WebSocketChatResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/21 10:23:08
 */
@Mapper
public interface WebsocketChatMapper extends BaseMapper<WebsocketChat> {

    List<WebSocketChatResponse> selectLastChat(@Param("userId") Integer userId);
}
