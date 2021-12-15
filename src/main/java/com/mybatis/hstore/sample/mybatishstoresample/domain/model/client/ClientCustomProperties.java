package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientCustomProperties {
    @NonNull
    @Getter
    private final List<ClientCustomProperty> list;

    public ClientCustomProperties(@NonNull List<ClientCustomProperty> list) {
       this.list = list;
    }

    public Optional<ClientCustomProperty> get(String key) {
        return this.getList().stream().filter(ccp -> ccp.getKey().equals(key)).findFirst();
    }
}
