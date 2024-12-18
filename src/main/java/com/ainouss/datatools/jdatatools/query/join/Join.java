package com.ainouss.datatools.jdatatools.query.join;

import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.From;
import lombok.Getter;

@Getter
public class Join<X, Y> {

    private final From target;
    private final From source;

    private final JoinType joinType;

    private Expression expression;

    private CriteriaQuery<?> subquery;


    public Join(From source, From target, JoinType joinType) {
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

    public String toSql() {
        return render();
    }

    public String render() {
        return new StringBuilder(this.getJoinType().name().toLowerCase())
                .append(" join ")
                .append(target.render())
                .append(" ")
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
