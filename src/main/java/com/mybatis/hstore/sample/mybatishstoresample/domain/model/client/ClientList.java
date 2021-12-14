package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;

public class ClientList {
    @NonNull
    @Getter
    private final List<Client> list;

    public ClientList(List<Client> list) {
        this.list = list;
    }
}
