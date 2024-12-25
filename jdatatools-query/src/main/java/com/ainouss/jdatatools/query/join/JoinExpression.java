package com.ainouss.jdatatools.query.join;

import com.ainouss.jdatatools.query.core.*;

/**
 * Join expression, intermediate operation that returns a join through on function
 * On clause is  mandatory all joins except cross join
 *
 * @param <T> source
 * @param <U> target
 */
public class JoinExpression<T, U> extends Alias {

    private final Join<T, U> join;


    public JoinExpression(Join<T, U> join) {
        this.join = join;
    }

    /**
     * On condition, not applicable to a cartesian product
     */
    public Join<T, U> on(AbstractExpression expression) {
        if (join.getJoinType() == JoinType.CROSS) {
            return join;
        }
        return join.on(expression);
    }

    public Join<T, U> on(CriteriaQuery<?> subquery) {
        return join.on(subquery);
    }


}
