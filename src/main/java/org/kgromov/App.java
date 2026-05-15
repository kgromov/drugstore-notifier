package org.kgromov;

import org.jobrunr.configuration.JobRunr;
import org.jobrunr.scheduling.cron.Cron;
import org.jobrunr.storage.InMemoryStorageProvider;

public class App {
    static void main(String[] args) {
        var messageSender = new TelegramMessageSender();
        var jobScheduler = JobRunr.configure()
                .useStorageProvider(new InMemoryStorageProvider())
                .useBackgroundJobServer()
                .useDashboard()
                .initialize()
                .getJobScheduler();
        jobScheduler.scheduleRecurrently(Cron.monthly(1), messageSender::notifyRecipientsOnExpired);
    }
}
