package com.example.chatting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter //유저들이 생성할 수 있도록 seetter로 열어둠
@Entity
public class ChattingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String chatName;

    //채팅방을 생성한 사람 (추가)
    @ManyToOne
    @JoinColumn(name = "creator_id") // 생성자를 나타내는 외래키
    private User creator; // 채팅방을 생성한 유저

}