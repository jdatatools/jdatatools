package com.ainouss.datatools.jdatatools.query.join;

import com.ainouss.datatools.jdatatools.query.core.*;
import lombok.Getter;

/**
 * Represents a join between two sources.
 *
 * @param <X> the type of the source.
 * @param <Y> the type of the target.
 */
@Getter
public class Join<X, Y> extends Alias implements Source, Joinable<Y> {

    private final Source target;
    private final Source source;
    private final JoinType joinType;
    private Expression expression;
    private CriteriaQuery<?> subquery;

    /**
     * Creates a join.
     *
     * @param source   the join source.
     * @param target   the join target.
     * @param joinType the type of the join.
     */
    public Join(Source source, Source target, JoinType joinType) {
        this.source = source;
        this.target = target;
        this.joinType = joinType;
    }

    public Join<X, Y> on(Expression on) {
        this.expression = on;
        return this;
    }

    public Join<X, Y> on(CriteriaQuery<?> subquery) {
        this.subquery = subquery;
        return this;
    }

    /**
     * Generates the SQL for this join.
     */
    public String toSql() {
        return new StringBuilder(source.getName())
                .append(" ")
                .append(source.getAlias())
                .append(" ")
                .append(this.getJoinType().name().toLowerCase())
                .append(" join ")
                .append(target.getName())
                .append(" ")
                .append(target.getAlias())
                .append(getStatement())
                .toString();
    }

    private String getStatement() {
        if (this.expression != null) {
            return " on " + expression.toSql();
        }
        if (this.subquery != null) {
            return " on (" + subquery.buildSelectQuery() + ")";
        }
        return "";
    }

    @Override
    public Selectable get(String attr) {
        return new Path<>(alias, attr);
    }

    @Override
    public Source getSelf() {
        return this;
    }
}
