package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.client;

import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.postgresql.util.HStoreConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientMapperTest {

    @Autowired
    private ClientMapper target;

    @Autowired
    private NamedParameterJdbcOperations jdbcOperations;

    @Test
     void 顧客情報登録テスト() {
        final var id = new ClientID("test_id");
        final var name = new ClientName("test_name");
        final var cp1 = new ClientCustomProperty("address", "address value.");
        final var cp2 = new ClientCustomProperty("tel_number", "090-0000-0000");
        final var properties = new ClientCustomProperties(Arrays.asList(cp1, cp2));
        Client client = new Client(id, name, properties);
        this.target.insert(client);

        final List<Client> result = jdbcOperations.query("SELECT * FROM client WHERE id = :id",
                new MapSqlParameterSource("id", client.getId().getValue()),
                (rs, rowNum) -> {
                    var _id = rs.getString("id");
                    var _name = rs.getString("name");
                    var _prop = HStoreConverter.fromString(rs.getString("custom_properties"));
                    var _propList =
                            _prop.entrySet().stream().map(e -> new ClientCustomProperty(e.getKey(), e.getValue())).collect(Collectors.toList());
                    return new Client(
                            new ClientID(_id),
                            new ClientName(_name),
                            new ClientCustomProperties((_propList))
                    );
                });

        assertEquals(1, result.size());
        final var firstRow = result.get(0);
        assertAll(
                () -> assertEquals(id.getValue(), firstRow.getId().getValue()),
                () -> assertEquals(name.getValue(), firstRow.getName().getValue()),
                () -> assertEquals(cp1.getId(), firstRow.getCustomProperties().getList().get(0).getId()),
                () -> assertEquals(cp1.getValue(), firstRow.getCustomProperties().getList().get(0).getValue()),
                () -> assertEquals(cp2.getId(), firstRow.getCustomProperties().getList().get(1).getId()),
                () -> assertEquals(cp2.getValue(), firstRow.getCustomProperties().getList().get(1).getValue())
        );
    }

    @Test
    void 顧客情報取得テスト() {
        final var id = new ClientID("test_id");
        final var name = new ClientName("test_name");
        final var cp1 = new ClientCustomProperty("address", "address value.");
        final var cp2 = new ClientCustomProperty("tel_number", "090-0000-0000");
        final var properties = new ClientCustomProperties(Arrays.asList(cp1, cp2));

        var customPropertiesMap = properties.getList().stream().collect(Collectors.toMap(ClientCustomProperty::getId, ClientCustomProperty::getValue));
        jdbcOperations.update("insert into client(id, name, custom_properties) values(:id, :name, hstore(:custom_properties))",
                new MapSqlParameterSource(Map.of("id", id.getValue(),
                                                 "name", name.getValue(),
                                                 "custom_properties", HStoreConverter.toString(customPropertiesMap))));

        var result = this.target.findByID(id);
        assertAll(
                () -> assertEquals(id.getValue(), result.getId()),
                () -> assertEquals(name.getValue(), result.getName()),
                () -> assertEquals(cp1.getValue(), result.getCustomProperties().get(cp1.getId())),
                () -> assertEquals(cp2.getValue(), result.getCustomProperties().get(cp2.getId()))
        );
    }

    @Test
    void 顧客情報削除テスト() {
        final var id = new ClientID("test_id");
        final var name = new ClientName("test_name");
        final var cp1 = new ClientCustomProperty("address", "address value.");
        final var cp2 = new ClientCustomProperty("tel_number", "090-0000-0000");
        final var properties = new ClientCustomProperties(Arrays.asList(cp1, cp2));

        var customPropertiesMap = properties.getList().stream().collect(Collectors.toMap(ClientCustomProperty::getId, ClientCustomProperty::getValue));
        jdbcOperations.update("insert into client(id, name, custom_properties) values(:id, :name, hstore(:custom_properties))",
                new MapSqlParameterSource(Map.of("id", id.getValue(),
                        "name", name.getValue(),
                        "custom_properties", HStoreConverter.toString(customPropertiesMap))));

        var resultBeforeDelete =
                jdbcOperations.queryForList("select * from client where id = :id", new MapSqlParameterSource("id", id.getValue()));
        assertEquals(1, resultBeforeDelete.size());

        this.target.delete(id);

        var resultAfterDelete =
                jdbcOperations.queryForList("select * from client where id = :id", new MapSqlParameterSource("id", id.getValue()));
        assertEquals(0, resultAfterDelete.size());
    }
}