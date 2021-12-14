package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.client;

import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class ClientRecord {
    private String id;
    private String name;
    private String[] customPropertyKeys;
    private String[] customPropertyValues;

    public Client toClient() {
        return new Client(new ClientID(id), new ClientName(name), convert2CustomProperties());
    }

    private ClientCustomProperties convert2CustomProperties() {
        List<ClientCustomProperty> list = IntStream.range(1, this.customPropertyKeys.length).mapToObj(i -> new ClientCustomProperty(customPropertyKeys[i], customPropertyValues[i])).collect(Collectors.toList());
        return new ClientCustomProperties(list);
    }
}