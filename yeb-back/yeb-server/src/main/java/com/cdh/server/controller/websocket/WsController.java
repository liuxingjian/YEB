package com.cdh.server.controller.websocket;

import com.cdh.server.pojo.Admin;
import com.cdh.server.pojo.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WsController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMessage chatMessage){
        Admin admin = (Admin) authentication.getPrincipal();
        chatMessage.setFrom(admin.getUsername());
        chatMessage.setNickName(admin.getName());
        chatMessage.setDate(LocalDateTime.now());
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getTo(),"/queue/chat",chatMessage);
    }

}
