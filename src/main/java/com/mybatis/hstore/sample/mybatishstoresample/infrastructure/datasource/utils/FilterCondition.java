package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.utils;

import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

enum FilterConditionType {
    EQUALS,
    BETWEEN,
    DATE_BETWEEN,
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
    @Getter
    LocalDate dateFrom;
    @Getter
    LocalDate dateTo;
    @Getter
    List<String> array;

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
            this.from = from;
            this.to = to;
            this.type = FilterConditionType.BETWEEN;
        }
    }

    public static class DateBetweenCondition extends FilterCondition {
        public DateBetweenCondition(String key, LocalDate dateFrom, LocalDate dateTo) {
            this.key = key;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
            this.type = FilterConditionType.DATE_BETWEEN;
        }
    }

    public static class ContainCondition extends FilterCondition {
        public ContainCondition(String key, List<String> array) {
            this.key = key;
            this.array = array;
            this.type = FilterConditionType.CONTAINS;
        }
    }

    public boolean isEquals() {
        return FilterConditionType.EQUALS.equals(type);
    }

    public boolean isBetween() {
        return FilterConditionType.BETWEEN.equals(type);
    }

    public boolean isDateBetween() {
        return FilterConditionType.DATE_BETWEEN.equals(type);
    }

    public boolean isContains() {
        return FilterConditionType.CONTAINS.equals(type);
    }
}

