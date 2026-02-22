package org.kgromov;

import java.sql.*;
import java.util.List;

public class App {
    static void main(String[] args) {
        final String URL = "jdbc:mysql://localhost:3306/drugstore";
        final String USER = "root";
        final String PASSWORD = "admin";


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement1 = connection.createStatement();
             Statement statement2 = connection.createStatement()) {

            var databaseName = "drugstore";
            try (ResultSet tables = connection.getMetaData().getTables(databaseName, databaseName, "%", new String[]{"TABLE"})) {
                System.out.println("Tables in the " + databaseName + ":");
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    System.out.println(tableName);
                }
            }

            var recipientRepository = new RecipientJdbcRepository();
            List<Recipient> recipients = recipientRepository.selectAll("SELECT * FROM Recipient");
            recipients.forEach(System.out::println);
            System.out.println(recipientRepository.count("SELECT * FROM Recipient"));

            ResultSet resultSet1 = statement1.executeQuery("SELECT * FROM Recipient");
            while (resultSet1.next()) {
                System.out.println(resultSet1.getRow() + " = " +
                        String.join(", ", String.valueOf(resultSet1.getLong("id")), resultSet1.getString("first_name")));
            }

            ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM DrugsInfo WHERE expiration_date < CURRENT_DATE");
            while (resultSet2.next()) {

            }
            resultSet1.close();
            resultSet2.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
