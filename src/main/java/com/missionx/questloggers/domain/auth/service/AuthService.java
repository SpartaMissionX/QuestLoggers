package com.missionx.questloggers.domain.auth.service;

import com.missionx.questloggers.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    public void login() {

    }
}
