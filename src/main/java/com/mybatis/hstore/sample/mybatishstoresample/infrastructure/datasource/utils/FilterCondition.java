package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.utils;

import lombok.Getter;

enum FilterConditionType {
    EQUALS,
    BETWEEN,
    CONTAINS
}

public abstract class FilterCondition {
    @Getter
    String key;
    @Getter
    String value;
    @Getter
    String from;
    @Getter
    String to;

    FilterConditionType type;

    public static class EqualsCondition extends FilterCondition {
        public EqualsCondition(String key, String value) {
            this.key = key;
            this.value = value;
            this.type = FilterConditionType.EQUALS;
        }
    }

    public boolean isEquals() {
        return FilterConditionType.EQUALS.equals(type);
    }

    public boolean isBetween() {
        return FilterConditionType.BETWEEN.equals(type);
    }

    public boolean isContains() {
        return FilterConditionType.CONTAINS.equals(type);
    }
}

