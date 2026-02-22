package org.kgromov;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

public abstract class JdbcRepository<T> {

    public Optional<T> selectOne(String query) {
        return this.selectOne(query, this.domainMapper());
    }

    public Optional<T> selectOne(String query, JdbcMapper<T> mapper) {
        try (ResultSet rs = this.select(query)) {
            var mapped = this.mapToModel(rs, mapper);
            if (mapped.size() > 1) {
                throw new IllegalStateException("Multiple records found for " + query);
            }
            return mapped.isEmpty() ? Optional.empty() : Optional.of(mapped.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> selectAll(String query) {
        return this.selectAll(query, this.domainMapper());
    }

    public List<T> selectAll(String query, JdbcMapper<T> mapper) {
        try(ResultSet rs = this.select(query)) {
            return this.mapToModel(rs, mapper);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int count(String query) {
        int count = 0;
        try (ResultSet rs = this.select(query)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract JdbcMapper<T> domainMapper();

    private ResultSet select(String sqlQuery) {
        return JdbcClient.getInstance().selectQuery(sqlQuery);
    }

    private <V> List<V> mapToModel(ResultSet rs, JdbcMapper<V> mapper) throws SQLException {
        if (isNull(rs)) {
            return emptyList();
        }
        List<V> result = new ArrayList<>();
        while (rs.next()) {
            result.add(mapper.mapToModel(rs));
        }
        return result;
    }
}
