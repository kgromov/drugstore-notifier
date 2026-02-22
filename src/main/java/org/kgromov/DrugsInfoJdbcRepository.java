package org.kgromov;

public class DrugsInfoJdbcRepository extends JdbcRepository<DrugsInfo> {

    @Override
    protected JdbcMapper<DrugsInfo> domainMapper() {
        return new DrugsIntoJdbcMapper();
    }
}
