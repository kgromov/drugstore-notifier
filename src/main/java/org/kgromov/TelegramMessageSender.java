package org.kgromov;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramMessageSender {
    private final DefaultAbsSender bot;
    private final RecipientJdbcRepository recipientRepository;
    private final DrugsInfoJdbcRepository drugsInfoRepository;

    public TelegramMessageSender() {
        var environment = Environment.getInstance();
        this.bot = new TelegramBot(environment.getProperty("TELEGRAM_BOT_TOKEN"));
        this.recipientRepository = new RecipientJdbcRepository();
        this.drugsInfoRepository = new DrugsInfoJdbcRepository();
    }

    public void notifyRecipientsOnExpired() {
        var recipients = recipientRepository.selectAll("SELECT * FROM Recipient");
        var expiredDrugs = drugsInfoRepository.selectAll("SELECT * FROM DrugsInfo WHERE expiration_date < CURRENT_DATE");
        expiredDrugs.forEach(drugsInfo -> {
            recipients.forEach(recipient -> this.sendMessage(recipient, drugsInfo));
        });
    }

    private void sendMessage(Recipient recipient, DrugsInfo drugsInfo) {
        System.out.printf("\nDrug %s expired on %s. Inform recipients.%n", drugsInfo.name(), drugsInfo.expirationDate());
        String message = "%s of category = %s is expired on %s".formatted(drugsInfo.name(), drugsInfo.category(), drugsInfo.expirationDate());
        SendMessage sendMessage = SendMessage.builder()
                .chatId(recipient.chatId())
                .text(message)
                .build();
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TelegramBot extends DefaultAbsSender {

        public TelegramBot(String token) {
            super(new DefaultBotOptions(), token);
        }
    }
}
