package com.cliptripbe.global.aop.idempotency.entity;

import com.cliptripbe.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "idempotency_keys")
public class IdempotencyKey extends BaseTimeEntity {

    @Id
    @Column(name = "idempotency_key")
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;


    @Lob
    @Column(name = "response_body", columnDefinition = "LONGTEXT")
    private String responseBody;


    public IdempotencyKey(String key) {
        this.key = key;
        this.status = RequestStatus.PROCESSING;

    }

    public void complete(String body) {
        this.status = RequestStatus.COMPLETED;
        this.responseBody = body;
    }
}

