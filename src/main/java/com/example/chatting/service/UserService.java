package com.example.chatting.service;

import com.example.chatting.dto.LoginRequest;
import com.example.chatting.dto.SignupRequest;
import com.example.chatting.entity.User;
import com.example.chatting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 0.회원가입
    public void signup(SignupRequest signupRequest) { 
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 암호화 후 db에 저장
        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();

        userRepository.save(user);
    }

    // 1.로그인
    @Transactional(readOnly = true)
    public boolean login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("회원이 아닙니다."));

        return passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()); //평문과 암호화된 비번 비교
    }

}
