package com.mybatis.hstore.sample.mybatishstoresample.domain.model.client;

import java.util.Optional;

public interface ClientRepository {
    void save(Client client);

    Optional<Client> findByID(ClientID id);

    ClientList findAll();
}
