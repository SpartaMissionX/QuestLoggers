package com.missionx.questloggers.domain.test.character;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestCharController {
    private final TestCharacterService testCharacterService;

    @PostMapping("/auth/test")
    public void createDummyChar() {
        testCharacterService.createUserAndChar();
    }

    @PostMapping("/auth/test/posts")
    public void createDummyPost() {
        testCharacterService.createPost();
    }
}
