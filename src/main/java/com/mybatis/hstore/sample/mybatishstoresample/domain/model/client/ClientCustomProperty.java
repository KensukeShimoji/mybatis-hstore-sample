package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import lombok.Getter;
import lombok.NonNull;

public class ClientCustomProperty {
    @NonNull
    @Getter
    private final String key;
    @NonNull
    @Getter
    private final String value;

    public ClientCustomProperty(@NonNull String key, @NonNull String value) {
        this.key = key;
        this.value = value;
    }
}
