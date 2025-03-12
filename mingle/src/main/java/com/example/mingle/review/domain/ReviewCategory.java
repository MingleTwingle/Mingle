package com.example.mingle.review.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReviewCategory {
/*    NOTICE("NOTICE"),
    REVIEW("REVIEW"),
    GENERAL("GENERAL"),*/
    숙소("숙소"),
    식당("식당");

    private final String value;

    ReviewCategory(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ReviewCategory fromValue(String value) {
        for (ReviewCategory category : ReviewCategory.values()) {
            if (category.value.equalsIgnoreCase(value)) { // ✅ 대소문자 무시 비교
                return category;
            }
        }
        throw new IllegalArgumentException("잘못된 카테고리 값: " + value);
    }
}
