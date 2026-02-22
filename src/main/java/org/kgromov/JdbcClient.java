package org.kgromov;

import java.sql.*;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JdbcClient {
    private static final JdbcClient instance = new JdbcClient();
    private final Queue<ConnectionResources> activeConnectionResources;

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
        this.activeConnectionResources = new LinkedBlockingQueue<>();
    }

    public static JdbcClient getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
    }

    public ResultSet selectQuery(String sqlQuery) {
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            this.activeConnectionResources.add(new ConnectionResources(connection, statement, resultSet));
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseResources() {
        var resource = this.activeConnectionResources.poll();
        while (nonNull(resource)) {
           this.close(resource.resultSet());
           this.close(resource.statement());
           this.close(resource.connection());
        }
    }

    private void close(AutoCloseable resource) {
        if (isNull(resource)) {
            return;
        }
        try {
            resource.close();
        } catch (Exception e) {
            System.out.println("Failed to close resource");
        }
    }

    record ConnectionResources(Connection connection, Statement statement, ResultSet resultSet) {}
}
