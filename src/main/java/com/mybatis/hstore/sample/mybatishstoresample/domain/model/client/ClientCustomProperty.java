package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import lombok.Getter;
import lombok.NonNull;

public class ClientCustomProperty {
    @NonNull
    @Getter
    private final String id;
    @NonNull
    @Getter
    private final String value;

    public ClientCustomProperty(String id, String value) {
        this.id = id;
        this.value = value;
    }
}
