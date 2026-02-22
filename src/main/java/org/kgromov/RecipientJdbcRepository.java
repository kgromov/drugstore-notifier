package org.kgromov;

public class RecipientJdbcRepository extends JdbcRepository<Recipient> {

    @Override
    protected JdbcMapper<Recipient> domainMapper() {
        return new RecipientJdbcMapper();
    }
}
