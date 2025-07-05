package com.cliptripbe.infrastructure.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileKind {
    BF_CULTURE_TOURISM("한국문화정보원_전국 배리어프리 문화예술관광지_20221125.csv"),
    RB_TURIST_THNG_DPSTRY_LCINFO(
        "RB_TURIST_THNG_DPSTRY_LCINFO_20221231.csv"),
    RB_DSPSN_TURIST_TURSM_INFO(
        "RB_DSPSN_TURIST_TURSM_INFO_20221231.csv");
    final String fileName;
}
