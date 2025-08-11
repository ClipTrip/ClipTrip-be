package com.cliptripbe.feature.user.domain.type;

public enum AgeGroup {
    CHILD(0, 9),
    AGE_10_14(10, 14),
    AGE_15_19(15, 19),
    AGE_20_24(20, 24),
    AGE_25_29(25, 29),
    AGE_30_34(30, 34),
    AGE_35_39(35, 39),
    AGE_40_44(40, 44),
    AGE_45_49(45, 49),
    AGE_50_54(50, 54),
    AGE_55_59(55, 59),
    AGE_60_64(60, 64),
    SENIOR(65, 120);

    private final int minAge;
    private final int maxAge;

    AgeGroup(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static AgeGroup from(int age) {
        for (AgeGroup group : values()) {
            if (age >= group.minAge && age <= group.maxAge) {
                return group;
            }
        }
        throw new IllegalArgumentException("Invalid age: " + age);
    }
}