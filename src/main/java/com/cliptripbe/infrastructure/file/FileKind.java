package com.cliptripbe.infrastructure.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileKind {
    BF_CULTURE_TOURISM("한국문화정보원_전국 배리어프리 문화예술관광지_20221125.csv");
    final String fileName;
}
