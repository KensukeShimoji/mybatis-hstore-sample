package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import lombok.Getter;
import lombok.NonNull;

public class ClientName {
    @NonNull
    @Getter
    private final String value;

    public ClientName(String value) {
        this.value = value;
    }
}
