package org.kgromov;

public class RecipientJdbcRepository implements JdbcRepository<Recipient> {

    @Override
    public JdbcMapper<Recipient> domainMapper() {
        return new RecipientJdbcMapper();
    }
}
