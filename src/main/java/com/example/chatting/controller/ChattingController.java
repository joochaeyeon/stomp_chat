package com.example.chatting.controller;

import com.example.chatting.dto.ChatMessage;
import com.example.chatting.dto.InviteRequest;
import com.example.chatting.repository.UserRepository;
import com.example.chatting.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    @MessageMapping("/chat/room/{roomId}/send")
    public void sendMessage(ChatMessage chatMessage) {
        chattingService.sendMessage(chatMessage);
    }


    @MessageMapping("/chat/room/{roomId}/leave")
    public void leaveRoom(ChatMessage chatMessage) {
        chattingService.handleLeave(chatMessage);
    }


    @MessageMapping("/chat/room/{roomId}/invite")
    public void inviteUser(@DestinationVariable Long roomId, @Payload InviteRequest inviteRequest) {
        chattingService.inviteUserToChatRoom(roomId, inviteRequest.getUserId());
    }

    @MessageMapping("/chat/message/read") //보내진 메세지를 읽기만 할 경우
    public void markMessageAsRead(ChatMessage chatMessage) {
        chattingService.markMessageAsRead(chatMessage.getMessageId(), chatMessage.getUserId());
    }

}
