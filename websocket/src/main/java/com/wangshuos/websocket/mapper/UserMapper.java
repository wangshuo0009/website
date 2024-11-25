package com.wangshuos.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangshuos.websocket.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/21 10:23:07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
