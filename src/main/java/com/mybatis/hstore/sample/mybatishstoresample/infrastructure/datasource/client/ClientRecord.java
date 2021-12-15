package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.client;

import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.*;
import lombok.Data;

@Data
public class ClientRecord {
    private String id;
    private String name;
    private ClientCustomProperties customProperties;

    public Client toClient() {
        return new Client(new ClientID(id), new ClientName(name), customProperties);
    }
}