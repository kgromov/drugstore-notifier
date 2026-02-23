package org.kgromov;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountJdbcMapper implements JdbcMapper<Integer> {

    @Override
    public Integer mapToModel(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(1);
    }
}
