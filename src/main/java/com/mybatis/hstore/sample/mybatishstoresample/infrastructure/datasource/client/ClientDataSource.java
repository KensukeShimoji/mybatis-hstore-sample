package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.client;

import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.Client;
import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.ClientID;
import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.ClientList;
import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.ClientRepository;
import com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.utils.FilterCondition;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClientDataSource implements ClientRepository {

    private final ClientMapper clientMapper;

    public ClientDataSource(ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
    }

    @Override
    public void save(Client client) {
        if (this.findByID(client.getId()).isEmpty()) {
            this.clientMapper.insert(client);
        } else {
            this.clientMapper.update(client);
        }
    }

    @Override
    public Optional<Client> findByID(ClientID id) {
        ClientRecord clientRecord = this.clientMapper.findByID(id);
        return Optional.ofNullable(clientRecord).map(ClientRecord::toClient);
    }

    @Override
    public ClientList findAll() {
        List<ClientRecord> clientRecord = this.clientMapper.findAll();
        List<Client> list = clientRecord.stream().map(ClientRecord::toClient).collect(Collectors.toList());
        return new ClientList(list);
    }
}