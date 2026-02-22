package org.kgromov;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

public interface JdbcRepository<T> {

    default T selectOne(String query) {
        return this.selectOne(query, this.domainMapper());
    }

    default T selectOne(String query, JdbcMapper<T> mapper) {
        ResultSet rs = this.select(query);
        return this.mapSingle(rs, mapper);
    }

    default List<T> selectAll(String query) {
        return this.selectAll(query, this.domainMapper());
    }

    default List<T> selectAll(String query, JdbcMapper<T> mapper) {
        ResultSet rs = this.select(query);
        return this.mapMultiple(rs, mapper);
    }

    default int count(String query) {
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

    JdbcMapper<T> domainMapper();

    private ResultSet select(String sqlQuery) {
        return JdbcClient.getInstance().selectQuery(sqlQuery);
    }

    default <V> V mapSingle(ResultSet rs, JdbcMapper<V> mapper) {
        try {
            if (isNull(rs) || !rs.next()) {
                return null;
            }
            return mapper.mapToModel(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    default <V> List<V> mapMultiple(ResultSet rs, JdbcMapper<V> mapper) {
        if (isNull(rs)) {
            return emptyList();
        }
        List<V> result = new ArrayList<>();
        try {
            while (rs.next()) {
                result.add(mapper.mapToModel(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
