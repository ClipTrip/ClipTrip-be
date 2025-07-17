package com.cliptripbe.feature.place.domain.converter;

import com.cliptripbe.feature.place.domain.type.AccessibilityFeature;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class AccessibilityFeatureConverter implements
    AttributeConverter<Set<AccessibilityFeature>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<AccessibilityFeature> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
            .map(Enum::name) // enum -> String
            .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<AccessibilityFeature> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays.stream(dbData.split(DELIMITER))
            .map(String::trim)
            .map(AccessibilityFeature::valueOf)
            .collect(Collectors.toSet());
    }
}