package com.cliptripbe.global.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CaptionDto {

    private double start;
    private double dur;
    private String text;
}
