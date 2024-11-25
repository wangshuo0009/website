package com.wangshuos.websocket.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/26 14:35:46
 */
@Getter
@Setter
@TableName("t_chat_file")
@Schema(name = "ChatFile", description = "")
public class ChatFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("path")
    private String path;
}
