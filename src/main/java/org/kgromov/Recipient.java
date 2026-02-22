package org.kgromov;

public record Recipient(
    int id,
    String firstName,
    String lastName,
    String phoneNumber,
    Long chatId
) {}
