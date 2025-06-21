package com.cliptripbe.infrastructure.file;

import static com.cliptripbe.infrastructure.file.FileKind.BF_CULTURE_TOURISM;

import com.cliptripbe.infrastructure.s3.S3FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileService {

    final S3FileReader fileReader;

    public String findPlaceInfo(String placeName) {
        try (BufferedReader br = fileReader.readCsv(BF_CULTURE_TOURISM.getFileName())) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(placeName)) {
                    return line;
                }
            }
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }
        throw new NoSuchElementException("존재하지 않는 장소입니다.");
    }
}
