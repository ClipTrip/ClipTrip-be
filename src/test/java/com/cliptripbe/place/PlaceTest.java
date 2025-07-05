package com.cliptripbe.place;

import static com.cliptripbe.infrastructure.file.FileKind.BF_CULTURE_TOURISM;

import com.cliptripbe.feature.place.application.PlaceMapper;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.infrastructure.s3.S3Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
@ActiveProfiles("local") // test 환경 설정 시 사용
public class PlaceTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private PlaceMapper placeMapper;

    @Test
    void registerPlace_실제CSV_저장확인() {
        List<Place> placeList = new ArrayList<>();
        try (BufferedReader br = s3Service.readCsv(BF_CULTURE_TOURISM.getFileName())) {
            String line;
            while ((line = br.readLine()) != null) {
                Place place = placeMapper.mapPlace(line);
                placeList.add(place);
            }
            placeRepository.saveAll(placeList);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }
    }
}
