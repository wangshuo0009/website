package com.wangshuos.websocket.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName UserList
 * @Author wangshuo
 * @Date 2024/8/21 14:12
 * @Version 1.0
 **/

@Data
public class UserListResponse {

    private Integer id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "帐号")
    private String account;

    @Schema(description = "密码")
    private String password;

    private String lastMessage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime lastMessageTime;
    private Integer unreadMessageCount;
    private String fileId;


}
