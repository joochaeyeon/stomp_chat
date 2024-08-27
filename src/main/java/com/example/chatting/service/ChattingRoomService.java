package com.example.chatting.service;

import com.example.chatting.entity.ChattingRoom;
import com.example.chatting.entity.JoinChatting;
import com.example.chatting.entity.User;
import com.example.chatting.repository.ChattingRoomRepository;
import com.example.chatting.repository.JoinChattingRepository;
import com.example.chatting.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;
    private final JoinChattingRepository joinChattingRepository;


    //0.채팅방 생성
    @Transactional
    public ChattingRoom createChatRoom(String chatName, Long creatorId, List<Long> userIds) {

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다 : " + creatorId));

        // 새로운 채팅방 생성
        ChattingRoom chattingRoom = new ChattingRoom();
        chattingRoom.setChatName(chatName);
        chattingRoom.setCreator(creator);
        chattingRoom = chattingRoomRepository.save(chattingRoom);

        // 채팅방 생성자를 JoinChatting에 추가
        JoinChatting creatorJoinChatting = new JoinChatting(creator, chattingRoom);
        joinChattingRepository.save(creatorJoinChatting);

        // 채팅방에 유저들 초대
        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다 : " + userId));
            JoinChatting joinChatting = new JoinChatting(user, chattingRoom);
            joinChattingRepository.save(joinChatting);
        }

        return chattingRoom;
    }


    // 원래 존재하던 채팅방에 새로운 유저 추가
    @Transactional
    public void addUserToChatRoom(Long roomId, List<Long> userIds) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다 : " + roomId));

        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다 : " + userId));


            // 이미 채팅방에 존재하는지 확인
            boolean isUserAlreadyInRoom = joinChattingRepository.existsByUserAndChattingRoom(user, chattingRoom);
            if (isUserAlreadyInRoom) {
                throw new IllegalArgumentException("유저가 이미 채팅방에 있습니다: " + user.getUsername());
            }


            JoinChatting joinChatting = new JoinChatting(user, chattingRoom);
            joinChattingRepository.save(joinChatting);
        }
    }

}