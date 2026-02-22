package org.kgromov;

import java.time.LocalDate;

public record DrugsInfo(
    int id,
    String name,
    DrugsForm form,
    Category category,
    Integer quantity,
    LocalDate expirationDate,
    String md5
) {}
