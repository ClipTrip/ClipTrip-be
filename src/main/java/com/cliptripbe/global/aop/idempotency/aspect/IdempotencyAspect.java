package com.cliptripbe.global.aop.idempotency.aspect;

import com.cliptripbe.global.aop.idempotency.entity.IdempotencyKey;
import com.cliptripbe.global.aop.idempotency.entity.RequestStatus;
import com.cliptripbe.global.aop.idempotency.repository.IdempotencyKeyRepository;
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
import org.springframework.http.HttpStatusCode;
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
                log.info("Returning stored response for key: {}", idempotencyKey);
                String storedResponseBody = storedKey.getResponseBody();

                // 2. 저장된 JSON을 실제 반환 타입의 객체로 역직렬화
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Class<?> returnType = signature.getReturnType();
                Object storedResponse = objectMapper.readValue(storedResponseBody, returnType);

                // 컨트롤러가 ResponseEntity를 직접 반환하는 경우를 위해 상태 코드도 사용
                // 여기서는 ApiResponse를 반환하므로 Spring이 자동으로 200 OK로 처리해줍니다.
                return storedResponse;
            } else { // PROCESSING
                log.warn("Request already in progress for key: {}", idempotencyKey);
                return new ResponseEntity<>("Request is already in progress.", HttpStatus.CONFLICT);
            }
        }

        try {
            idempotencyKeyRepository.save(new IdempotencyKey(idempotencyKey));
        } catch (DataIntegrityViolationException e) {
            log.warn("Concurrent request detected by unique constraint for key: {}",
                idempotencyKey);
            return new ResponseEntity<>("Request is already in progress.", HttpStatus.CONFLICT);
        }

        try {
            // 3. 반환 타입을 Object로 변경하여 모든 타입의 응답을 받음
            Object response = joinPoint.proceed();

            // 4. 응답 객체를 JSON 문자열로 직렬화
            String responseBodyJson = objectMapper.writeValueAsString(response);

            IdempotencyKey keyToComplete = idempotencyKeyRepository.findById(idempotencyKey)
                .orElseThrow(() -> new IllegalStateException(
                    "Idempotency key not found after processing: " + idempotencyKey));

            // 응답이 ResponseEntity인 경우 실제 상태 코드를, 아닌 경우 200 OK를 사용
//            HttpStatusCode statusCode = (response instanceof ResponseEntity)
//                ? ((ResponseEntity<?>) response).getStatusCode()
//                : HttpStatus.OK;

            keyToComplete.complete(responseBodyJson);
            idempotencyKeyRepository.save(keyToComplete);

            return response;
        } catch (Exception e) {
            log.error("Error during processing for key: {}. Removing key.", idempotencyKey, e);
            idempotencyKeyRepository.deleteById(idempotencyKey);
            throw e;
        }
    }

}
