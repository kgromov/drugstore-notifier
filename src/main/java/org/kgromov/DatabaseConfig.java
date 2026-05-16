package org.kgromov;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

class DatabaseConfig {
    private static final DatabaseConfig instance = new DatabaseConfig();
    private final MysqlConnectionPoolDataSource dataSource;

    private DatabaseConfig() {
        this.dataSource = this.createDataSource();
    }

    private MysqlConnectionPoolDataSource createDataSource() {
        var environment = Environment.getInstance();
        String databaseName = environment.getProperty("DATASOURCE_DATABASE", "drugstore");
        String defaultDatasourceUrl = "jdbc:mysql://localhost:3306/%s".formatted(databaseName);
        String dbUrl = environment.getProperty("DATASOURCE_URL", defaultDatasourceUrl);
        String dbUser = environment.getProperty("DATASOURCE_USERNAME", "root");
        String dbPassword = environment.getProperty("DATASOURCE_PASSWORD", "admin");
//        MysqlDataSource dataSource = new MysqlDataSource();
        var dataSource = new MysqlConnectionPoolDataSource();
        // Basic connection properties
        dataSource.setURL(dbUrl);
        dataSource.setUser(dbUser);
        dataSource.setPassword(dbPassword);
        try {
            dataSource.setServerTimezone("UTC");
            dataSource.setUseSSL(false);
            dataSource.setAllowPublicKeyRetrieval(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        dataSource.getConnection();
        return dataSource.getPooledConnection().getConnection();
    }

    public static DatabaseConfig getInstance() {
        return instance;
    }
}
