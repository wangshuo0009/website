package com.wangshuos.websocket.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/21 10:23:07
 */
@Data
@TableName("t_user")
@Schema(name = "User", description = "")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "帐号")
    @TableField("account")
    private String account;

    @Schema(description = "密码")
    @TableField("password")
    private String password;
}
