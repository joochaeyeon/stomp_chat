package com.example.chatting.controller;

import com.example.chatting.dto.ChatRoomRequest;
import com.example.chatting.entity.ChattingRoom;
import com.example.chatting.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatting")
public class ChatRoomController {

    private final ChattingRoomService chattingRoomService;


    //새로운 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<ChattingRoom> createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        ChattingRoom chatRoom = chattingRoomService.createChatRoom(
                chatRoomRequest.getChatName(),
                chatRoomRequest.getCreatorId(),
                chatRoomRequest.getUserIds()
        );
        return ResponseEntity.ok(chatRoom);
    }
    
    //기존에 있는 채팅방에 새로운 유저 추가
    @PostMapping("/room/{roomId}/add-users")
    public ResponseEntity<String> addUserToChatRoom(@PathVariable Long roomId, @RequestBody List<Long> userIds) {
        chattingRoomService.addUserToChatRoom(roomId, userIds);
        return ResponseEntity.ok("새로운 유저를 채팅방에 초대 성공");
    }
}
