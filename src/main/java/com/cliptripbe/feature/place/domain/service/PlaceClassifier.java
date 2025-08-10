package com.cliptripbe.feature.place.domain.service;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.vo.Address;
import com.cliptripbe.feature.place.dto.request.LuggageStorageRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlaceClassifier {

    private static final Double EARTH_RADIUS = 6371000.0; // 단위: meters;
    private static final Integer CLASSIFY_RANGE = 2000; // 2km 이내

    public List<Place> getLuggagePlacesByRange(
        LuggageStorageRequest luggageStorageRequest,
        List<Place> luggageStoragePlaces
    ) {
        double latitude = Math.toRadians(luggageStorageRequest.latitude());
        double longitude = Math.toRadians(luggageStorageRequest.longitude());
        List<Place> luggagePlaces = new ArrayList<>();

        for (Place place : luggageStoragePlaces) {
            Address addr = place.getAddress();
            double targetLat = Math.toRadians(addr.latitude());
            double targetLon = Math.toRadians(addr.longitude());

            double distance = haversineDistance(latitude, longitude, targetLat, targetLon);

            if (distance <= CLASSIFY_RANGE) {
                luggagePlaces.add(place);
            }
        }
        return luggagePlaces;
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2)
            + Math.cos(lat1) * Math.cos(lat2)
            * Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }
}