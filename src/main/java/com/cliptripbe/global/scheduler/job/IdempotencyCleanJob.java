package com.cliptripbe.global.scheduler.job;

import com.cliptripbe.global.scheduler.service.IdempotencyCleanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyCleanJob {

    private final IdempotencyCleanService idempotencyCleanService;

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanIdempotencyKeys() {
        log.info("멱등키 정리 작업 시작");
        int deletedCount = idempotencyCleanService.deleteExpiredKeys();
        log.info("멱등키 정리 작업 완료. 삭제된 키 개수: {}", deletedCount);
    }
}
