package com.cliptripbe.infrastructure.inital.initaler;


import static com.cliptripbe.infrastructure.file.FileKind.BF_CULTURE_TOURISM;

import com.cliptripbe.feature.place.application.PlaceMapper;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.infrastructure.file.FileKind;
import com.cliptripbe.infrastructure.s3.S3Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceInitializer {

    private final PlaceRepository placeRepository;

    private final S3Service s3Service;

    private final PlaceMapper placeMapper;

    public void registerCulturePlace() {
        List<Place> placeList = new ArrayList<>();
        try (BufferedReader br = s3Service.readCsv(BF_CULTURE_TOURISM.getFileName())) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                Place place = placeMapper.mapPlaceCulture(line);
                placeList.add(place);
            }
            placeRepository.saveAll(placeList);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }
    }

    public void registerStoragePlace() {
        List<Place> placeList = new ArrayList<>();
        try (BufferedReader br = s3Service.readCsv(
            FileKind.RB_TURIST_THNG_DPSTRY_LCINFO.getFileName())) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                Place place = placeMapper.mapPlaceThng(line);
                placeList.add(place);
            }
            placeRepository.saveAll(placeList);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }
    }
}
