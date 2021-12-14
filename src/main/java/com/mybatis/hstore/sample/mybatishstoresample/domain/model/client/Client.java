package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import lombok.Getter;

public class Client {
    @Getter
    ClientID id;

    @Getter
    ClientName name;

    @Getter
    ClientCustomProperties customProperties;

    public Client(ClientName name, ClientCustomProperties customProperties) {
        this(ClientID.generate(), name, customProperties );
    }

    public Client(ClientID id, ClientName name, ClientCustomProperties customProperties) {
        this.id = id;
        this.name = name;
        this.customProperties = customProperties;
    }
}
