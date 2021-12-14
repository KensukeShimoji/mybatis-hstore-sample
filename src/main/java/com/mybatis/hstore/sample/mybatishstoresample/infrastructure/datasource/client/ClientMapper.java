package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.client;

import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.Client;
import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.ClientID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientMapper {

    List<ClientRecord> findAll();

    ClientRecord findByID(@Param("id") ClientID id);

    int insert(@Param("client") Client client);

    int update(@Param("client") Client client);

    int delete(@Param("id") ClientID id);
}