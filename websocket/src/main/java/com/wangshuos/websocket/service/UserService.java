package com.wangshuos.websocket.service;

import com.wangshuos.websocket.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangshuos.websocket.response.UserListResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangshuo
 * @since 2024/08/21 10:23:07
 */
public interface UserService extends IService<User> {

    List<UserListResponse> searchUserList(Integer signInUserId);

}
