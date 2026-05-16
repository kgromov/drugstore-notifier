package org.kgromov;

import org.jobrunr.configuration.JobRunr;
import org.jobrunr.scheduling.cron.Cron;
import org.jobrunr.storage.sql.mysql.MySqlStorageProvider;

public class App {
    static void main(String[] args) {
        var messageSender = new TelegramMessageSender();
        var jobScheduler = JobRunr.configure()
                .useStorageProvider(new MySqlStorageProvider(DatabaseConfig.getInstance().getDataSource()))
                .useBackgroundJobServer()
                .useDashboard()             // http://localhost:8000/dashboard
                .initialize()
                .getJobScheduler();
        jobScheduler.scheduleRecurrently(Cron.monthly(1), messageSender::notifyRecipientsOnExpired);
    }
}
