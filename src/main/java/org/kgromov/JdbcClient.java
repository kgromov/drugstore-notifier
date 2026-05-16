package org.kgromov;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JdbcClient {
    private static final JdbcClient instance = new JdbcClient();


    private JdbcClient() {
    }

    public static JdbcClient getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DatabaseConfig.getInstance().getConnection();
    }

    public <T> List<T> selectQuery(String sqlQuery, JdbcMapper<T> mapper, Object... args) {
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            if (nonNull(args)) {
                for (int i = 0; i < args.length; i++) {
                    statement.setObject(i + 1, args[i]);
                }
            }
            ResultSet resultSet = statement.executeQuery();
            return this.mapToModel(resultSet, mapper);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <V> List<V> mapToModel(ResultSet rs, JdbcMapper<V> mapper) throws SQLException {
        if (isNull(rs)) {
            return emptyList();
        }
        List<V> result = new ArrayList<>();
        while (rs.next()) {
            result.add(mapper.mapToModel(rs));
        }
        return result;
    }
}
