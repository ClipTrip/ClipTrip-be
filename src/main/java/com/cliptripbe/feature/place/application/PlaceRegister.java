package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.api.dto.PlaceDto;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.infrastructure.file.FileService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceRegister {

    final FileService fileService;
    final PlaceMapper placeMapper;
    final PlaceRepository placeRepository;

    public Place registerPlace(String placeName) {
        String placeInfo = fileService.findPlaceInfo(placeName);
        Place place = placeMapper.mapPlace(placeInfo);
        placeRepository.save(place);
        return place;
    }

    public List<Place> registerAllPlaces(List<PlaceDto> placeDtoList) {
        List<Place> entities = placeDtoList.stream()
            .map(PlaceDto::toPlace)
            .toList();

        return placeRepository.saveAll(entities);
    }
}
