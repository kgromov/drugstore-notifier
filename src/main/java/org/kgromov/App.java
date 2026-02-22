package org.kgromov;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class App {
    static void main(String[] args) {
        final String URL = "jdbc:mysql://localhost:3306/drugstore";
        final String USER = "root";
        final String PASSWORD = "admin";

        var messageSender = new TelegramMessageSender();
        var startOfNextMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()).atStartOfDay();
        var calendar = GregorianCalendar.from(ZonedDateTime.of(startOfNextMonth, ZoneId.systemDefault()));
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                messageSender.notifyRecipientsOnExpired();
            }
        }, calendar.getTime(), TimeUnit.DAYS.toMillis(30));


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM Recipient");
             Statement statement2 = connection.createStatement()) {

            ResultSet resultSet1 = statement1.executeQuery();
            while (resultSet1.next()) {
                System.out.println(resultSet1.getRow() + " = " +
                        String.join(", ", String.valueOf(resultSet1.getLong("id")), resultSet1.getString("first_name")));
            }

            var recipientRepository = new RecipientJdbcRepository();
            List<Recipient> recipients = recipientRepository.selectAll("SELECT * FROM Recipient");
            recipients.forEach(System.out::println);
            System.out.println(recipientRepository.count("SELECT * FROM Recipient"));


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
