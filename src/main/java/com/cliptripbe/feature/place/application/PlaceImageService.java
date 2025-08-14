package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.infrastructure.port.google.PlaceImageProviderPort;
import com.cliptripbe.infrastructure.port.s3.FileStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceImageService {

    private static final String S3_PLACE_PREFIX = "place/";

    private final FileStoragePort fileStoragePort;
    private final PlaceImageProviderPort placeImageProviderPort;


    public void savePlaceImage(Place place) {
        String searchKeyWord = place.getName() + " " + place.getAddress().roadAddress();
        byte[] imageBytes = placeImageProviderPort.getPhotoByAddress(searchKeyWord);
        String imageUrl = fileStoragePort.upload(S3_PLACE_PREFIX, imageBytes);
        place.addImageUrl(imageUrl);
    }
}
