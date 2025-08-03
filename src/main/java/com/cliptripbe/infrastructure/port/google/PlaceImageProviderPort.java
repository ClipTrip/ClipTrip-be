package com.cliptripbe.infrastructure.port.google;

public interface PlaceImageProviderPort {

    byte[] getPhotoByAddress(String address);
}
