package com.example.chatting.repository;

import com.example.chatting.entity.ChattingRoom;
import com.example.chatting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
