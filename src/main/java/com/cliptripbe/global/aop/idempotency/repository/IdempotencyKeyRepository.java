package com.cliptripbe.global.aop.idempotency.repository;

import com.cliptripbe.global.aop.idempotency.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {

}
