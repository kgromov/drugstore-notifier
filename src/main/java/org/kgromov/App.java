package org.kgromov;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class App {
    static void main(String[] args) {
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

    }
}
