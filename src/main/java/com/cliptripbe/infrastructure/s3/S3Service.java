package com.cliptripbe.infrastructure.s3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Component
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final Region awsRegion;

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

    public String upload(String key, byte[] data) {
        String filename = UUID.randomUUID().toString() + ".png";
        String fullKey = key + filename;
        PutObjectRequest req = PutObjectRequest.builder()
            .bucket(bucket)
            .key(fullKey)
            .contentType("image/png")
            .build();

        s3Client.putObject(req, RequestBody.fromBytes(data));
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
            bucket,
            awsRegion.id(),
            fullKey
        );
    }
}
