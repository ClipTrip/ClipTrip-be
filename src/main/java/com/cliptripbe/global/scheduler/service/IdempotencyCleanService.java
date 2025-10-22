package com.cliptripbe.global.scheduler.service;

import com.cliptripbe.global.aop.idempotency.repository.IdempotencyKeyRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotencyCleanService {

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    @Transactional
    public int deleteExpiredKeys() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);
        int  deletedCount = idempotencyKeyRepository.deleteByCreatedAtBefore(threshold);
        log.info("만료된 멱등키 삭제 완료 - 삭제된 개수: {}, 기준 시간: {}", deletedCount, threshold);
        return deletedCount;
    }
}
