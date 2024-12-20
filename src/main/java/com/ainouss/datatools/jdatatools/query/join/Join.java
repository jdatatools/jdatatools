package com.ainouss.datatools.jdatatools.query.join;

import com.ainouss.datatools.jdatatools.query.core.*;
import lombok.Getter;

@Getter
public class Join<X, Y> extends NoAlias implements Source {

    private final Source target;
    private final Source source;
    private final JoinType joinType;
    private Expression expression;
    private CriteriaQuery<?> subquery;

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

    public Join<X, Y> rightJoin(Root<?> target) {
        return new Join<>(this, target, JoinType.RIGHT);
    }

    public String toSql() {
        return new StringBuilder(source.render())
                .append(" ")
                .append(source.getAlias())
                .append(" ")
                .append(this.getJoinType().name().toLowerCase())
                .append(" join ")
                .append(target.render())
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
}
