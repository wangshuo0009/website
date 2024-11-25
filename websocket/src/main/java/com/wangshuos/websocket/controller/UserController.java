package com.wangshuos.websocket.controller;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wangshuos.common.Result;
import com.wangshuos.common.constant.CommonConstant;
import com.wangshuos.websocket.entity.User;
import com.wangshuos.websocket.response.UserListResponse;
import com.wangshuos.websocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName UserController
 * @Author wangshuo
 * @Date 2024/8/21 11:06
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    //@Autowired
    //private WebsocketChatService websocketChatService;
    @GetMapping("/searchAllUser")
    public Result<List<UserListResponse>> searchAllUser(@CookieValue(value = CommonConstant.X_USER_ID) Integer signInUserId){
        List<UserListResponse> userListResponses = userService.searchUserList(signInUserId);
        return Result.success("查询成功", userListResponses);
    }

    @GetMapping("/searchUserById/{userId}")
    public Result<User> searchUserById(@PathVariable("userId") Integer userId){
        User user = userService.getById(userId);
        return Result.success("查询成功", user);
    }
    @GetMapping("/searchUserByAccount/{account}")
    public Result<User> searchUserByAccount(@PathVariable("account") String account){
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
        return Result.success("查询成功", user);
    }

    @PostMapping("/addUser")
    public Result<User> searchUserByAccount(@RequestBody User user){
        User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAccount, user.getAccount()));
        if (one == null){
            user.setId(null);
            user.setPassword(MD5.create().digestHex(user.getPassword()));
            userService.save(user);
        } else {
            return Result.fail("账户已存在");
        }
        return Result.success("新增成功");
    }

}
