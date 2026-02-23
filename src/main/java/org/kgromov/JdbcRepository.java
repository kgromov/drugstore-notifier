package org.kgromov;

import java.util.List;
import java.util.Optional;

public abstract class JdbcRepository<T> {

    public Optional<T> selectOne(String query, Object... args) {
        return this.selectOne(query, this.domainMapper(), args);
    }

    public <V> Optional<V> selectOne(String query, JdbcMapper<V> mapper, Object... args) {
        var result = this.selectAll(query, mapper, args);
        if (result.size() > 1) {
            throw new IllegalStateException("Multiple records found for " + query);
        }
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    public List<T> selectAll(String query, Object... args) {
        return JdbcClient.getInstance().selectQuery(query, this.domainMapper(), args);
    }

    public <V> List<V> selectAll(String query, JdbcMapper<V> mapper, Object... args) {
        return JdbcClient.getInstance().selectQuery(query, mapper, args);
    }

    public int count(String query, Object... args) {
        return this.selectOne(query, new CountJdbcMapper(), args)
                .orElseThrow(() -> new IllegalArgumentException("Query should contain COUNT"));
    }

    protected abstract JdbcMapper<T> domainMapper();
}
