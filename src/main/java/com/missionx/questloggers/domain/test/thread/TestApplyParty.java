package com.missionx.questloggers.domain.test.thread;

import com.missionx.questloggers.domain.test.party.TestApplyPartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestApplyParty {

    private final TestApplyPartyService partyService;

    // 동시성 테스트 파라미터
    private static final int THREADS = 50;              // 스레드풀 크기
    private static final int REQUESTS = 200;            // 총 요청 수
    private static final long DONE_TIMEOUT_SEC = 30;    // 완료 대기 타임아웃
    private static final long SHUTDOWN_TIMEOUT_SEC = 10;

    @GetMapping("/test/apply-party")
    public String testApplyParty() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        // 동시에 출발하도록 스타트 게이트 & 완료 대기 래치
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(REQUESTS);

        AtomicInteger started  = new AtomicInteger(0);   // 시작된 요청 수
        AtomicInteger completed = new AtomicInteger(0);  // 끝난 요청 수
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failure = new AtomicInteger(0);

        for (long i = 1; i <= REQUESTS; i++) {
            final long userId = i;              // 서로 다른 사용자 시뮬레이션
            final int reqNo = (int) i;          // 요청 고유 번호 (로그용)
            // 경합을 더 세게 만들고 싶으면 아래로 고정:
            // final long userId = 1L;

            executor.submit(() -> {
                try {
                    start.await(); // 모두 큐잉된 뒤 동시에 출발

                    int s = started.incrementAndGet();
                    log.info("[START] req#{}/{} userId={}", reqNo, REQUESTS, userId);
                    log.info("[COUNT] started={}/{}", s, REQUESTS);

                    // 필요 시 지터로 스케줄 섞기
                    // Thread.sleep(ThreadLocalRandom.current().nextInt(5));

                    partyService.applyPartyResponseDto(userId);
                    success.incrementAndGet();

                    int c = completed.incrementAndGet();
                    log.info("[END]   req#{}/{} userId={} status=SUCCESS completed={}/{}",
                            reqNo, REQUESTS, userId, c, REQUESTS);

                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    int c = completed.incrementAndGet();
                    failure.incrementAndGet();
                    log.warn("[END]   req#{}/{} userId={} status=INTERRUPTED completed={}/{}",
                            reqNo, REQUESTS, userId, c, REQUESTS);

                } catch (Exception e) {
                    int c = completed.incrementAndGet();
                    failure.incrementAndGet();
                    log.warn("[END]   req#{}/{} userId={} status=FAIL({}) completed={}/{}",
                            reqNo, REQUESTS, userId, e.toString(), c, REQUESTS);

                } finally {
                    done.countDown();
                }
            });
        }

        // 일제히 시작
        start.countDown();

        boolean finished = done.await(DONE_TIMEOUT_SEC, TimeUnit.SECONDS);

        executor.shutdown();
        if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_SEC, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }

        log.info("[SUMMARY] total={}, success={}, failure={}, finishedWithinTimeout={}",
                REQUESTS, success.get(), failure.get(), finished);

        return String.format(
                "Concurrent apply finished: total=%d, success=%d, failure=%d, threads=%d, completedWithinTimeout=%s",
                REQUESTS, success.get(), failure.get(), THREADS, finished
        );
    }
}
