package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.utils.handler;

import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.ClientCustomProperties;
import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.ClientCustomProperty;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.HStoreConverter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class HstoreTypeHandler extends BaseTypeHandler<ClientCustomProperties> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ClientCustomProperties parameter, JdbcType jdbcType) throws SQLException {
        var map = parameter.getList().stream().collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        ps.setString(i, HStoreConverter.toString(map));
    }

    @Override
    public ClientCustomProperties getNullableResult(ResultSet rs, String columnName) throws SQLException {
        var map = HStoreConverter.fromString(rs.getString(columnName));
        var list = map.entrySet().stream().map(p -> new ClientCustomProperty(p.getKey(), p.getValue())).collect(Collectors.toList());
        return new ClientCustomProperties(list);
    }

    @Override
    public ClientCustomProperties getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        var map = HStoreConverter.fromString(rs.getString(columnIndex));
        var list = map.entrySet().stream().map(p -> new ClientCustomProperty(p.getKey(), p.getValue())).collect(Collectors.toList());
        return new ClientCustomProperties(list);
    }

    @Override
    public ClientCustomProperties getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        var map = HStoreConverter.fromString(cs.getString(columnIndex));
        var list = map.entrySet().stream().map(p -> new ClientCustomProperty(p.getKey(), p.getValue())).collect(Collectors.toList());
        return new ClientCustomProperties(list);
    }
}
