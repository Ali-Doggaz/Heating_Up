package com.example.Balayage.client;

import javax.swing.tree.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRowMapper implements RowMapper {

    public static final String ID_COLUMN = "id";
    public static final String NATIONALITY_COLUMN = "nationalite";
    public static final String AGE_COLUMN = "age";
    public static final String INCOME_COLUMN = "revenus";

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();

        client.setId(rs.getInt(ID_COLUMN));
        client.setNationalite(rs.getString(NATIONALITY_COLUMN));
        client.setAge(rs.getInt(AGE_COLUMN));
        client.setRevenus(rs.getDouble(INCOME_COLUMN));

        return client;
    }
}
