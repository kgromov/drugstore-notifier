package org.kgromov;

public class DrugsInfoJdbcRepository implements JdbcRepository<DrugsInfo> {

    @Override
    public JdbcMapper<DrugsInfo> domainMapper() {
        return new DrugsIntoJdbcMapper();
    }
}
