package org.kgromov;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipientJdbcMapper implements JdbcMapper<Recipient> {

    @Override
    public Recipient mapToModel(ResultSet resultSet) throws SQLException {
        return new Recipient(
            resultSet.getInt("id"),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"),
            resultSet.getString("phone_number"),
            resultSet.getLong("chatId")
        );
    }
}
