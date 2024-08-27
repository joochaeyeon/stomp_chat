package com.example.chatting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class JoinChatting {
    //관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없기 때문에
    //연결 테이블을 중간에 추가해서 다대일 관계로 풀어내는게 바람직한 해결 방법
    //따라서 User와 ChattingRoom사이에 joinChatting 테이블을 두었다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChattingRoom chattingRoom;

    public JoinChatting(User user, ChattingRoom chattingRoom) {
        this.user = user;
        this.chattingRoom = chattingRoom;
    }
}
