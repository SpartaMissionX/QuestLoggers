package com.missionx.questloggers.domain.test.character;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestCharController {
    private final TestCharacterService testCharacterService;

    @PostMapping("/auth/test")
    public void createDummyChar() {
        testCharacterService.createUserAndChar();
    }
}
