package com.cliptripbe.infrastructure.port.s3;

import java.io.BufferedReader;

public interface S3Port {

    BufferedReader readCsv(String key);
    String upload(String key, byte[] data);
}
