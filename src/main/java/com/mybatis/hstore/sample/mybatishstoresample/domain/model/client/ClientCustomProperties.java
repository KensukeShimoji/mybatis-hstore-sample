package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public class ClientCustomProperties {
    @NonNull
    @Getter
    private final List<ClientCustomProperty> list;

    public ClientCustomProperties(List<ClientCustomProperty> list) {
       this.list = list;
    }

    public String[] getKeys() {
        return this.list.stream().map(v -> v.getId()).toArray(String[]::new);
    }

    public String[] getValues() {
        return this.list.stream().map(v -> v.getValue()).toArray(String[]::new);
    }
}
