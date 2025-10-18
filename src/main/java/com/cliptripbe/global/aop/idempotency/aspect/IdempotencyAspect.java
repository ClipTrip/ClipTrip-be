package com.cliptripbe.global.aop.idempotency.aspect;

import static com.cliptripbe.global.response.type.ErrorType.REQUEST_ALREADY_IN_PROGRESS;

import com.cliptripbe.global.aop.idempotency.entity.IdempotencyKey;
import com.cliptripbe.global.aop.idempotency.entity.RequestStatus;
import com.cliptripbe.global.aop.idempotency.repository.IdempotencyKeyRepository;
import com.cliptripbe.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotencyAspect {

    private static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";

    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final ObjectMapper objectMapper;


    @Around("@annotation(com.cliptripbe.global.aop.idempotency.annotation.Idempotent)")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object handleIdempotency(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final String idempotencyKey = request.getHeader(IDEMPOTENCY_KEY_HEADER);

        if (idempotencyKey == null || idempotencyKey.isEmpty()) {
            return joinPoint.proceed();
        }

        var existingKey = idempotencyKeyRepository.findById(idempotencyKey);
        if (existingKey.isPresent()) {
            IdempotencyKey storedKey = existingKey.get();
            if (storedKey.getStatus() == RequestStatus.COMPLETED) {
                // COMPLETED
                log.info("저장된 응답을 반환합니다. 키: {}", idempotencyKey);
                String storedResponseBody = storedKey.getResponseBody();

                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Class<?> returnType = signature.getReturnType();
                Object storedResponse = objectMapper.readValue(storedResponseBody, returnType);

                return storedResponse;
            } else {
                // PROCESSING
                log.info("이미 처리 중인 요청입니다. 키: {}", idempotencyKey);
                return ApiResponse.error(REQUEST_ALREADY_IN_PROGRESS);
            }
        }

        try {
            // PROCESSING (신규 요청)
            idempotencyKeyRepository.save(new IdempotencyKey(idempotencyKey));
        } catch (DataIntegrityViolationException e) {
            log.warn("동시에 들어온 요청이 감지되었습니다(유니크 제약 조건 충돌). 키: {}", idempotencyKey);
            return ApiResponse.error(REQUEST_ALREADY_IN_PROGRESS);
        }

        try {
            // 비지니스 로직 후 응답
            Object response = joinPoint.proceed();
            String responseBodyJson = objectMapper.writeValueAsString(response);

            // 데이터 정합성 or 외부에서 중간에 키 값을 지웠을 때
            IdempotencyKey keyToComplete = idempotencyKeyRepository.findById(idempotencyKey)
                .orElseThrow(() -> new IllegalStateException(
                    "요청 처리 후에도 Idempotency 키를 찾을 수 없습니다. 키: " + idempotencyKey));

            keyToComplete.complete(responseBodyJson);
            idempotencyKeyRepository.save(keyToComplete);

            return response;
        } catch (Exception e) {
            log.error("요청 처리 중 오류 발생. 키: {} → 키를 삭제합니다.", idempotencyKey, e);
            idempotencyKeyRepository.deleteById(idempotencyKey);
            throw e;
        }
    }

}
