package org.kgromov;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcMapper<T> {

    T mapToModel(ResultSet resultSet) throws SQLException;
}
