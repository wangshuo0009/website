package com.wangshuos.websocket.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
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
@TableName("t_websocket_chat")
@Schema(name = "WebsocketChat", description = "")
public class WebsocketChat implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @Schema(description = "内容")
    @TableField("content")
    private String content;

    @Schema(description = "发送人")
    @TableField("send_user_id")
    private Integer sendUserId;

    @Schema(description = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 指定序列和反序列化，解决转换为json问题
    private LocalDateTime createTime;

    @Schema(description = "接收人")
    @TableField("accept_user_id")
    private Integer acceptUserId;

    @Schema(description = "是否已读")
    @TableField("is_reader")
    private Boolean isReader;

    @Schema(description = "图片地址")
    @TableField("file_id")
    private Integer fileId;
}
