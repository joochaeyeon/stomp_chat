package com.example.chatting.controller;

import com.example.chatting.dto.SignupRequest;
import com.example.chatting.dto.LoginRequest;
import com.example.chatting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 0.회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest){
        userService.signup(signupRequest);
        return ResponseEntity.ok("회원가입 완료");

    }
    
    // 1.로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        boolean isAuth = userService.login(loginRequest);
        if(isAuth){
            return ResponseEntity.ok("로그인 성공");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }

    }
}
