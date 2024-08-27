package com.example.chatting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry config) {
        // [주소] ws://localhost:8080/ws-stomp
        config.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*");
//        withSockJS()를 추가하면 클라이언트가 SockJS 프로토콜을 사용해야 한다. 그렇지 않으면 연결이 실패
//                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/sub"); //server -> clients (->브로드캐스트할 때)
        config.setApplicationDestinationPrefixes("/pub");   //client->server
    }

}
