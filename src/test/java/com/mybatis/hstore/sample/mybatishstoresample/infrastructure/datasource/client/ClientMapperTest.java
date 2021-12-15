package com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.client;

import com.mybatis.hstore.sample.mybatishstoresample.domain.model.client.*;
import com.mybatis.hstore.sample.mybatishstoresample.infrastructure.datasource.utils.FilterCondition;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.postgresql.util.HStoreConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.time.LocalDate;
import java.util.ArrayList;
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
                () -> assertEquals(cp1.getKey(), firstRow.getCustomProperties().getList().get(0).getKey()),
                () -> assertEquals(cp1.getValue(), firstRow.getCustomProperties().getList().get(0).getValue()),
                () -> assertEquals(cp2.getKey(), firstRow.getCustomProperties().getList().get(1).getKey()),
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

        var customPropertiesMap = properties.getList().stream().collect(Collectors.toMap(ClientCustomProperty::getKey, ClientCustomProperty::getValue));
        jdbcOperations.update("insert into client(id, name, custom_properties) values(:id, :name, hstore(:custom_properties))",
                new MapSqlParameterSource(Map.of("id", id.getValue(),
                        "name", name.getValue(),
                        "custom_properties", HStoreConverter.toString(customPropertiesMap))));

        var result = this.target.findByID(id);
        assertAll(
                () -> assertEquals(id.getValue(), result.getId()),
                () -> assertEquals(name.getValue(), result.getName()),
                () -> assertEquals(cp1.getValue(), result.getCustomProperties().get(cp1.getKey()).get().getValue()),
                () -> assertEquals(cp2.getValue(), result.getCustomProperties().get(cp2.getKey()).get().getValue())
        );
    }

    @Test
    void 顧客情報削除テスト() {
        final var id = new ClientID("test_id");
        final var name = new ClientName("test_name");
        final var cp1 = new ClientCustomProperty("address", "address value.");
        final var cp2 = new ClientCustomProperty("tel_number", "090-0000-0000");
        final var properties = new ClientCustomProperties(Arrays.asList(cp1, cp2));

        var customPropertiesMap = properties.getList().stream().collect(Collectors.toMap(ClientCustomProperty::getKey, ClientCustomProperty::getValue));
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

    @Test
    void 顧客情報抽出テスト_等価() {

        final var id = new ClientID("test_id");
        final var name = new ClientName("test_name");
        final var cps = new ClientCustomProperties(
                Arrays.asList(
                        new ClientCustomProperty("address", "大阪府"),
                        new ClientCustomProperty("tel_number", "090-0000-0000")));
        this.target.insert(new Client(id, name, cps));

        final var id2 = new ClientID("test_id2");
        final var name2 = new ClientName("test_name2");
        final var cps2 = new ClientCustomProperties(
                Arrays.asList(
                        new ClientCustomProperty("address", "東京都"),
                        new ClientCustomProperty("tel_number", "090-0000-0000")));

        this.target.insert(new Client(id2, name2, cps2));

        List<FilterCondition> fcs = new ArrayList<>();
        FilterCondition fc1 = new FilterCondition.EqualsCondition("address", "東京都");
        fcs.add(fc1);
        var result = this.target.filter(fcs);
        assertEquals(1, result.size());
    }

    @Test
    void 顧客情報抽出テスト_範囲_数値() {

        final var id = new ClientID("test_id");
        final var name = new ClientName("test_name");
        final var cps = new ClientCustomProperties(
                Arrays.asList(
                        new ClientCustomProperty("address", "大阪府"),
                        new ClientCustomProperty("number_of_employee", "100")));
        this.target.insert(new Client(id, name, cps));

        final var id2 = new ClientID("test_id2");
        final var name2 = new ClientName("test_name2");
        final var cps2 = new ClientCustomProperties(
                Arrays.asList(
                        new ClientCustomProperty("address", "東京都"),
                        new ClientCustomProperty("number_of_employee", "10")));

        this.target.insert(new Client(id2, name2, cps2));

        final var id3 = new ClientID("test_id3");
        final var name3 = new ClientName("test_name3");
        final var cps3 = new ClientCustomProperties(
                Arrays.asList(
                        new ClientCustomProperty("address", "東京都"),
                        new ClientCustomProperty("number_of_employee", "101")));

        this.target.insert(new Client(id3, name3, cps3));

        List<FilterCondition> fcs = new ArrayList<>();
        FilterCondition fc1 = new FilterCondition.BetweenCondition("number_of_employee", 11, 100);
        fcs.add(fc1);
        var result = this.target.filter(fcs);
        assertEquals(1, result.size());
    }

    @Test
    void 顧客情報抽出テスト_範囲_日付() {

        final var id = new ClientID("test_id");
        final var name = new ClientName("test_name");
        final var cps = new ClientCustomProperties(
                Arrays.asList(
                        new ClientCustomProperty("address", "大阪府"),
                        new ClientCustomProperty("date", "2021-11-01")));
        this.target.insert(new Client(id, name, cps));

        final var id2 = new ClientID("test_id2");
        final var name2 = new ClientName("test_name2");
        final var cps2 = new ClientCustomProperties(
                Arrays.asList(
                        new ClientCustomProperty("address", "東京都"),
                        new ClientCustomProperty("date", "2021-10-10")));

        this.target.insert(new Client(id2, name2, cps2));

        final var id3 = new ClientID("test_id3");
        final var name3 = new ClientName("test_name3");
        final var cps3 = new ClientCustomProperties(
                Arrays.asList(
                        new ClientCustomProperty("address", "東京都"),
                        new ClientCustomProperty("date", "2021-11-10")));

        this.target.insert(new Client(id3, name3, cps3));

        List<FilterCondition> fcs = new ArrayList<>();
        FilterCondition fc1 = new FilterCondition.DateBetweenCondition("date", LocalDate.of(2021,10,30), LocalDate.of(2021,11,2));
        fcs.add(fc1);
        var result = this.target.filter(fcs);
        assertEquals(1, result.size());
    }
}