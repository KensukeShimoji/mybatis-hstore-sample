package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientCustomProperties {
    @NonNull
    @Getter
    private final List<ClientCustomProperty> list;

    public ClientCustomProperties(@NonNull List<ClientCustomProperty> list) {
       this.list = list;
    }

    public Map<String, String> getMap() {
        return this.list.stream().collect(Collectors.toMap(ClientCustomProperty::getId, ClientCustomProperty::getValue));
    }

    public String[] getKeys() {
        return this.list.stream().map(ClientCustomProperty::getId).toArray(String[]::new);
    }

    public String[] getValues() {
        return this.list.stream().map(ClientCustomProperty::getValue).toArray(String[]::new);
    }
}
