package com.example.chatting.service;

import com.example.chatting.dto.ChatMessage;
import com.example.chatting.entity.ChattingRoom;
import com.example.chatting.entity.JoinChatting;
import com.example.chatting.entity.Message;
import com.example.chatting.entity.User;
import com.example.chatting.repository.ChattingRoomRepository;
import com.example.chatting.repository.JoinChattingRepository;
import com.example.chatting.repository.MessageRepository;
import com.example.chatting.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final JoinChattingRepository joinChattingRepository;

    //메세지 브로드캐스팅할때 사용됨
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendMessage(ChatMessage chatMessage) {
        User user = userRepository.findById(chatMessage.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        ChattingRoom room = chattingRoomRepository.findById(chatMessage.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // 채팅방에 초대된 구성원이 맞는지 검증
        boolean isMember = joinChattingRepository.existsByUserAndChattingRoom(user, room);
        if (!isMember) {
            throw new IllegalArgumentException("해당 채팅방의 구성원이 아닙니다.");
        }

        chatMessage.setType(ChatMessage.MessageType.CHAT);

        Long unreadCount = joinChattingRepository.countByChattingRoom(room) - 1;

        Message message = Message.builder()
                .content(chatMessage.getContent())
                .unreadCount(unreadCount) //보냈을 때는 바로 보진 않으니 기본이 False
                .user(user)
                .chattingRoom(room)
                .build();

        messageRepository.save(message);

        // 이전 메시지들의 unreadCount를 업데이트
        messageRepository.decrementUnreadCountForPreviousMessages(room, message.getId());

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }


    //유저들의 메세지 읽음 여부 표시하기 위해
    @Transactional
    public void markMessageAsRead(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

        // 사용자가 메시지를 읽었을 때 unreadCount를 감소
        message.decrementUnreadCnt();

        messageRepository.save(message);
    }


    @Transactional
    public void inviteUserToChatRoom(Long roomId, Long userId) { //새로운 유저를 채팅방에 초대할 경우
        ChattingRoom room = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 채팅방에 유저를 초대
        JoinChatting joinChatting = new JoinChatting(newUser, room);
        joinChattingRepository.save(joinChatting);

        // 입장 메시지 전송
        ChatMessage welcomeMessage = new ChatMessage();
        welcomeMessage.setContent(newUser.getUsername() + "님이 입장하셨습니다.");
        welcomeMessage.setRoomId(roomId);
        welcomeMessage.setType(ChatMessage.MessageType.ENTER);

        messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, welcomeMessage);
    }

    @Transactional
    public void handleLeave(ChatMessage chatMessage) { //유저가 채팅방에서 나갔을 경우
        chatMessage.setContent(chatMessage.getUsername() + "님이 퇴장하셨습니다.");
        chatMessage.setType(ChatMessage.MessageType.LEAVE);

        // 퇴장 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }



}
