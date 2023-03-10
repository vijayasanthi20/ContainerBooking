package com.project.codingexcercise;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import reactor.core.publisher.Mono;

public class MaxValueFinder {
    private final CqlSession session;

    public MaxValueFinder(CqlSession session) {
        this.session = session;
    }

    public Mono<Long> findMaxValue(String tableName, String primaryKeyColumnName) {
        String query = String.format("SELECT MAX(%s) FROM %s", primaryKeyColumnName, tableName);
        SimpleStatement statement = SimpleStatement.newInstance(query);

        return Mono.fromCompletionStage(session.executeAsync(statement))
                .flatMap(result -> Mono.justOrEmpty(result.one()))
                .map(row -> row.getLong(0));
    }
    
}
