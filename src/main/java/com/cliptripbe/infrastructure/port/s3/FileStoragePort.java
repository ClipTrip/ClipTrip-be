package com.cliptripbe.infrastructure.port.s3;

import java.io.BufferedReader;

public interface FileStoragePort {

    BufferedReader readCsv(String key);
    String upload(String key, byte[] data);
}
