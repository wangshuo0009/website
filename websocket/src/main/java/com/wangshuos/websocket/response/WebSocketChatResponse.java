package com.wangshuos.websocket.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName WebSocketChatResponse
 * @Author wangshuo
 * @Date 2024/8/21 14:53
 * @Version 1.0
 **/
@Data
public class WebSocketChatResponse {
    private Integer id;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "发送人")
    private Integer sendUserId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    @Schema(description = "接收人")
    private Integer acceptUserId;

    private Integer account;
    private Boolean isReader;
    private Integer unreadMessageCount;
    private String fileId;
}
