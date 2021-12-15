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
    Integer from;
    @Getter
    Integer to;

    FilterConditionType type;

    public static class EqualsCondition extends FilterCondition {
        public EqualsCondition(String key, String value) {
            this.key = key;
            this.value = value;
            this.type = FilterConditionType.EQUALS;
        }
    }

    public static class BetweenCondition extends FilterCondition {
        public BetweenCondition(String key, Integer from, Integer to) {
            this.key = key;
            this.from= from;
            this.to = to;
            this.type = FilterConditionType.BETWEEN;
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

