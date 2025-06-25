package com.cliptripbe.feature.place.domain;

import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class AccessibilityFeatureConverter implements
    AttributeConverter<List<AccessibilityFeature>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<AccessibilityFeature> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
            .map(Enum::name) // enum -> String
            .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<AccessibilityFeature> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(dbData.split(DELIMITER))
            .map(String::trim)
            .map(AccessibilityFeature::valueOf)
            .collect(Collectors.toList());
    }
}