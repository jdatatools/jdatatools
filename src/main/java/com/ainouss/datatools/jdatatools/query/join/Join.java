package com.ainouss.datatools.jdatatools.query.join;

import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Root;
import com.ainouss.datatools.jdatatools.query.core.Source;
import lombok.Getter;

@Getter
public class Join<X, Y> implements Source {

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
        return render();
    }

    public String render() {
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

    @Override
    public String getAlias() {
        return "";
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
    public void setAlias(String alias) {

    }
}
