package com.cliptripbe.infrastructure.port.s3;

import java.io.BufferedReader;
import java.time.Duration;

public interface FileStoragePort {

    BufferedReader readCsv(String key);

    String upload(String key, byte[] data);

    String generatePresignedUrlForDownload(String key, Duration expiration);
}
