package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

public class ClientID {
    @NonNull
    @Getter
    private final String value;

    public ClientID(String value) {
        this.value = value;
    }

    static ClientID generate() {
        return new ClientID(UUID.randomUUID().toString());
    }
}
