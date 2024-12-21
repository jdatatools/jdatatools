package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.expression.IdentityExpression;
import com.ainouss.datatools.jdatatools.query.join.Join;
import com.ainouss.datatools.jdatatools.query.join.JoinExpression;
import com.ainouss.datatools.jdatatools.query.join.JoinType;

public interface Joinable<T> extends Fragment{

    Source getSelf();

    /**
     * Left join expression
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */
    default <U> JoinExpression<T, U> leftJoin(Source root) {
        Join<T, U> join = new Join<>(getSelf(), root, JoinType.LEFT);
        return new JoinExpression<>(join);
    }

    /**
     * Right join expression
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */
    default <U> JoinExpression<T, U> rightJoin(Source root) {
        Join<T, U> join = new Join<>(getSelf(), root, JoinType.RIGHT);
        return new JoinExpression<>(join);
    }

    /**
     * Inner join expression
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */
    default <U> JoinExpression<T, U> innerJoin(Source root) {
        Join<T, U> join = new Join<>(getSelf(), root, JoinType.INNER);
        return new JoinExpression<>(join);
    }

    /**
     * Full join expression
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */

    default <U> JoinExpression<T, U> fullJoin(Source root) {
        Join<T, U> join = new Join<>(getSelf(), root, JoinType.FULL);
        return new JoinExpression<>(join);
    }

    /**
     * Cross join expression, does not have on clause, thus returns a join directly
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */
    default <U> Join<T, U> join(Source root) {
        Join<T, U> join = new Join<>(getSelf(), root, JoinType.CROSS);
        return new JoinExpression<>(join).on(new IdentityExpression());
    }
}
