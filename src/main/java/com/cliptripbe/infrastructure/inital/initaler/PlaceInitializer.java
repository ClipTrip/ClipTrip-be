package com.cliptripbe.infrastructure.inital.initaler;


import com.cliptripbe.feature.place.application.PlaceFinder;
import com.cliptripbe.feature.place.application.mapper.PlaceCsvMapper;
import com.cliptripbe.feature.place.application.mapper.PlaceMapper;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.infrastructure.inital.type.DefaultData;
import com.cliptripbe.infrastructure.s3.S3Service;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceInitializer {

    private final PlaceRepository placeRepository;
    private final S3Service s3Service;
    private final PlaceMapper placeMapper;
    private final PlaceFinder placeFinder;

    private final List<PlaceCsvMapper> csvMappers;
    private Map<DefaultData, PlaceCsvMapper> mapperMap;

    @PostConstruct
    public void initMapperMap() {
        mapperMap = csvMappers.stream()
            .collect(Collectors.toMap(
                PlaceCsvMapper::getDefaultData,  // 예시
                Function.identity()
            ));
    }

    public List<Place> registerPlace(DefaultData defaultData) {
        List<Place> placeList = new ArrayList<>();
        Set<String> uniquePlaceKeys = new HashSet<>();

        try (BufferedReader br = s3Service.readCsv(defaultData.getFileName())) {
            PlaceCsvMapper placeCsvMapper = mapperMap.get(defaultData);

            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                Place newPlace = placeCsvMapper.map(line);
                String uniqueKey =
                    newPlace.getName() + "::" + newPlace.getAddress().roadAddress();
                if (uniquePlaceKeys.contains(uniqueKey)) {
                    continue;
                }
                uniquePlaceKeys.add(uniqueKey);
                Place place = getOrCreateByLine(newPlace);
                placeList.add(place);
            }
            placeRepository.saveAll(placeList);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }
        return placeList;
    }

    public List<Place> registerFourCoulmn(DefaultData defaultData) {
        List<Place> placeList = new ArrayList<>();
        try (BufferedReader br = s3Service.readCsv(
            defaultData.getFileName())) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                Place place = placeMapper.mapPlaceFour(line, defaultData.getPlaceType());
                placeList.add(place);
            }
            placeRepository.saveAll(placeList);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }
        return placeList;
    }

    private Place getOrCreateByLine(Place place) {
        Optional<Place> optionalPlace = placeFinder.findByNameAndRoadAddress(
            place.getName(),
            place.getAddress().roadAddress()
        );
        Place placeToProcess;
        if (optionalPlace.isPresent()) {
            placeToProcess = optionalPlace.get();
            placeToProcess.addAccessibilityFeatures(place.getAccessibilityFeatures());
        } else {
            placeToProcess = place;
        }
        return placeToProcess;
    }
}
