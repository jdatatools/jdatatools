package com.ainouss.datatools.jdatatools.query.join;

import com.ainouss.datatools.jdatatools.query.core.*;

/**
 * Join expression, intermediate operation that returns a join through on function
 * On clause is  mandatory all joins except cross join
 *
 * @param <T> source
 * @param <U> target
 */
public class JoinExpression<T, U> extends Alias implements Source {

    private final Join<T, U> join;


    public JoinExpression(Join<T, U> join) {
        this.join = join;
    }

    /**
     * On condition, not applicable to a cartesian product
     */
    public Join<T, U> on(Expression expression) {
        if (join.getJoinType() == JoinType.CROSS) {
            return join;
        }
        return join.on(expression);
    }

    public Join<T, U> on(CriteriaQuery<?> subquery) {
        return join.on(subquery);
    }

    @Override
    public String toSql() {
        return join.toSql();
    }

    @Override
    public Selectable get(String attr) {
        return new Path<>(attr, attr);
    }

    @Override
    public String getName() {
        return getAlias();
    }
}
