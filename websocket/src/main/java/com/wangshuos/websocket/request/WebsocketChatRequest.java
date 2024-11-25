package com.wangshuos.websocket.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/21 10:23:08
 */
@Data
public class WebsocketChatRequest {
    private Integer id;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "发送人")
    private Integer sendUserId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 指定序列和反序列化，解决转换为json
    private LocalDateTime createTime;

    @Schema(description = "接收人")
    private Integer acceptUserId;

    @Schema(description = "是否已读")
    private Boolean isReader;
}
