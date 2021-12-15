package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.client;

import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.Client;
import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.ClientID;
import com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.utils.FilterCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientMapper {

    List<ClientRecord> findAll();

    ClientRecord findByID(@Param("id") ClientID id);

    List<ClientRecord> filter(@Param("conditions") List<FilterCondition> conditions);

    int insert(@Param("client") Client client);

    int update(@Param("client") Client client);

    int delete(@Param("id") ClientID id);
}