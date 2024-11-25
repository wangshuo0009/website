package com.wangshuos.websocket.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangshuos.common.Result;
import com.wangshuos.common.constant.CommonConstant;
import com.wangshuos.common.util.FileUtils;
import com.wangshuos.websocket.entity.ChatFile;
import com.wangshuos.websocket.entity.WebsocketChat;
import com.wangshuos.websocket.server.WebsocketNotRedisNacosKafkaServer;
import com.wangshuos.websocket.service.ChatFileService;
import com.wangshuos.websocket.service.WebsocketChatService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

import static com.wangshuos.common.util.FileUtils.createFilePath;
import static com.wangshuos.common.util.TimeUtil.getNow;

/**
 * @ClassName WebsocketChatController
 * @Author wangshuo
 * @Date 2024/8/21 10:38
 * @Version 1.0
 **/
@RestController
@RequestMapping("/chatMessage")
@Log4j2
public class WebsocketChatController {
    @Autowired
    private WebsocketChatService websocketChatService;
    //@Autowired
    //private WebsocketChatServer websocketChatServer;
    @Autowired
    private WebsocketNotRedisNacosKafkaServer websocketNotRedisNacosKafkaServer;
    @Autowired
    private ChatFileService chatFileService;

    private String filePath = "/Users/wangshuo/IdeaProjects/website/websocket/images";
    @GetMapping("/searchMessage")
    public Result<?> searchMessage(@CookieValue(CommonConstant.X_USER_ID) Integer searchUserAccount,
                                   @RequestParam("acceptUserId") Integer acceptUserId,
                                   @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        Page<WebsocketChat> page = new Page<>(pageNum, pageSize);
        websocketChatService.page(page, new LambdaQueryWrapper<WebsocketChat>()
                .eq(WebsocketChat::getSendUserId, searchUserAccount)
                .eq(WebsocketChat::getAcceptUserId, acceptUserId)
                .or()
                .eq(WebsocketChat::getSendUserId, acceptUserId)
                .eq(WebsocketChat::getAcceptUserId, searchUserAccount)
                .orderByDesc(WebsocketChat::getCreateTime)
        );
        page.setRecords(page.getRecords()
                .stream()
                .peek( r -> {
                    if (StringUtils.isBlank(r.getContent()) && r.getFileId() != null) {
                        r.setContent(FileUtils.fileToBase64(new File(chatFileService.getById(r.getFileId()).getPath())));
                    }
                })
                .sorted(Comparator.comparing(WebsocketChat::getCreateTime))
                .collect(Collectors.toList())
        );
        // 启动异步任务更新消息状态
        websocketChatService.updateWebsocketChatIsReader(page.getRecords(), searchUserAccount);

        return Result.success("查询成功", page);
    }



    //@PostMapping("/sendMessageForAcceptUserId")
    //public void sendMessageForAcceptUserId(@RequestHeader("acceptUserId") String acceptUserId,@RequestBody List<String> stringList) {
    //    log.error("接收到请求");
    //    try {
    //        List<WebsocketChat> websocketChatList = new ArrayList<>();
    //        ObjectMapper objectMapper = new ObjectMapper();
    //
    //        for (String websocketChatStr : stringList) {
    //            WebsocketChat websocketChat = objectMapper.readValue(websocketChatStr, WebsocketChat.class);
    //            websocketChatList.add(websocketChat);
    //        }
    //        websocketChatServer.sendMessageToUser(acceptUserId, objectMapper.writeValueAsString(websocketChatList));
    //    } catch (JsonProcessingException e) {
    //        e.printStackTrace();
    //    }
    //}

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/file/{acceptUserId}")
    public Result<?> file(@CookieValue(CommonConstant.X_USER_ID) Integer sendUserId,
                          @PathVariable Integer acceptUserId,
                          @RequestPart("file") MultipartFile file) {

        String finalPath = createFilePath(filePath, getNow() + "_" + file.getOriginalFilename());
        // 将文件写入本地路径
        try {
            File localFile = new File(finalPath);
            localFile.getParentFile().mkdirs(); // 创建父目录
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                fos.write(file.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("文件写入失败", e);
        }
        ChatFile chatFile = new ChatFile() {{
            setName(file.getOriginalFilename());
            setPath(finalPath);
        }};
        chatFileService.save(chatFile);
        websocketNotRedisNacosKafkaServer.sendMessageToUser(new WebsocketChat(){{
            setSendUserId(sendUserId);
            setAcceptUserId(acceptUserId);
            setFileId(chatFile.getId());
            setCreateTime(LocalDateTime.now());
            setIsReader(false);
        }});

        return Result.success();
    }


}
