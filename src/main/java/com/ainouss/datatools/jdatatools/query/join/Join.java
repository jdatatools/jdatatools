package com.ainouss.datatools.jdatatools.query.join;

import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;
import com.ainouss.datatools.jdatatools.query.core.Root;
import lombok.Getter;

@Getter
public class Join<X, Y> extends Path<X> {

    private final Root<Y> target;

    private final JoinType joinType;

    private Expression expression;

    private CriteriaQuery<?> subquery;


    public Join(Root<X> source, Root<Y> target, JoinType joinType) {
        super(source, null);
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

    @Override
    public String toString() {
        return render();
    }

    public String render() {
        return new StringBuilder(this.getJoinType().name().toLowerCase())
                .append(" join ")
                .append(target.schema())
                .append(target.getTable())
                .append(" ")
                .append(target.getAlias())
                .append(" ")
                .append(getStatement())
                .toString();
    }

    private String getStatement() {
        if (this.expression != null) {
            return " on " + expression;
        }
        if (this.subquery != null) {
            return " on (" + subquery.buildSelectQuery() + ")";
        }
        return "";
    }

}
