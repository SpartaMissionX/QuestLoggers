package com.missionx.questloggers.domain.test.thread;

import com.missionx.questloggers.domain.test.party.TestApplyPartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestApplyParty {
    private final TestApplyPartyService partyService;

    @GetMapping("/test/apply-party")
    public String testApplyParty() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10); // 동시 10개 실행
        CountDownLatch latch = new CountDownLatch(100); // 100명 기다리기

        for (long i = 1; i <= 100; i++) {
            long userId = i; // userId 1~100 (여기 맞게 수정)
            executor.submit(() -> {
                try {
                    partyService.applyPartyResponseDto(userId);
                } catch (Exception e) {
                    System.out.println("Error for userId " + userId + ": " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 작업이 끝날 때까지 대기
        executor.shutdown();

        return "100 API called concurrently.";
    }
}