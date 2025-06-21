package com.cliptripbe.infrastructure.s3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Component
@RequiredArgsConstructor
public class S3FileReader {

    private final S3Client s3Client;

    @Value("${s3.bucket}")
    private String bucket;

    public BufferedReader readCsv(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        ResponseInputStream<GetObjectResponse> s3Stream = s3Client.getObject(request);
        return new BufferedReader(new InputStreamReader(s3Stream, StandardCharsets.UTF_8));
    }
}
