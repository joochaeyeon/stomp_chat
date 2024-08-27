package com.example.chatting.repository;

import com.example.chatting.entity.ChattingRoom;
import com.example.chatting.entity.JoinChatting;
import com.example.chatting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinChattingRepository extends JpaRepository<JoinChatting, Long> {
    boolean existsByUserAndChattingRoom(User user, ChattingRoom chattingRoom);
    Long countByChattingRoom(ChattingRoom chattingRoom);
}
