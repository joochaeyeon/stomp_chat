package com.example.chatting.repository;

import com.example.chatting.entity.ChattingRoom;
import com.example.chatting.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Modifying
    @Query("UPDATE Message m SET m.unreadCount = m.unreadCount - 1 " +
            "WHERE m.chattingRoom = :room AND m.id < :messageId AND m.unreadCount > 0")
    void decrementUnreadCountForPreviousMessages(@Param("room") ChattingRoom room, @Param("messageId") Long messageId);

}
