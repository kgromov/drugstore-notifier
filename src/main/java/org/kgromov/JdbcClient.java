package org.kgromov;

import java.sql.*;

public class JdbcClient {
    private static final JdbcClient instance = new JdbcClient();

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private JdbcClient() {
        var environment = Environment.getInstance();
        String databaseName = environment.getProperty("DATASOURCE_DATABASE", "drugstore");
        String defaultDatasourceUrl = "jdbc:mysql://localhost:3306/%s".formatted(databaseName);
        this.dbUrl = environment.getProperty("DATASOURCE_URL", defaultDatasourceUrl);
        this.dbUser = environment.getProperty("DATASOURCE_USERNAME", "root");
        this.dbPassword = environment.getProperty("DATASOURCE_PASSWORD", "admin");
    }

    public static JdbcClient getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
    }

    public ResultSet selectQuery(String sqlQuery) {
  /*      try (Connection connection = this.getConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
