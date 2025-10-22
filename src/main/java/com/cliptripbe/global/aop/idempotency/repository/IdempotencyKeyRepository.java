package com.cliptripbe.global.aop.idempotency.repository;

import com.cliptripbe.global.aop.idempotency.entity.IdempotencyKey;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {

    @Modifying
    @Query("DELETE FROM IdempotencyKey i WHERE i.expiresAt <= :threshold")
    int deleteByCreatedAtBefore(@Param("threshold") LocalDateTime threshold);
}
