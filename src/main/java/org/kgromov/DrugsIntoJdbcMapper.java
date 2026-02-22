package org.kgromov;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DrugsIntoJdbcMapper implements JdbcMapper<DrugsInfo> {

    @Override
    public DrugsInfo mapToModel(ResultSet resultSet) throws SQLException {
        return new DrugsInfo(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            DrugsForm.valueOf(resultSet.getString("form")),
            Category.valueOf(resultSet.getString("category")),
            resultSet.getInt("quantity"),
            resultSet.getDate("expiration_date").toLocalDate(),
            resultSet.getString("md5")
        );
    }
}
