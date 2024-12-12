package com.ainouss.datatools.jdatatools.query;

import com.ainouss.datatools.jdatatools.query.expression.*;
import com.ainouss.datatools.jdatatools.query.function.*;
import com.ainouss.datatools.jdatatools.query.operator.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * Used to construct criteria queries, selections, expressions, predicates, orderings.
 */
public class CriteriaBuilder {
    public CriteriaBuilder() {
    }

    /**
     * @param clazz class
     * @param <T>   type
     * @return new CriteriaQuery
     */
    public <T> CriteriaQuery<T> createQuery(Class<T> clazz) {
        return new CriteriaQuery<>(clazz);
    }

    /**
     * @param path  pth
     * @param value value
     * @return like expression
     */
    public Expression like(Path<?> path, Object value) {
        return new Like(path, value);
    }

    /**
     * @param path pth
     * @param str  value
     * @return like expression
     */
    public Expression endsWith(Path<?> path, String str) {
        return new EndsWith(path, str);
    }

    /**
     * @param path pth
     * @param str  value
     * @return like expression
     */
    public Expression startsWith(Path<?> path, String str) {
        return new StartsWith(path, str);
    }


    /**
     * @param path  path
     * @param value value
     * @return eq expression
     */
    public Expression eq(Path<?> path, Object value) {
        return new Eq(path, value);
    }

    /**
     * @param path       path
     * @param expression expression
     * @return eq expression
     */
    public Expression eq(Path<?> path, Path<?> expression) {
        return new Eq(path, expression);
    }

    /**
     * When expression, depends
     *
     * @param predicate condition
     * @return conditional expression
     */
    public PredicateExpression when(boolean predicate) {
        return new PredicateExpression(predicate);
    }

    /**
     * Greater than expression
     *
     * @param path  path
     * @param value value
     * @return greater than
     */
    public Expression gt(Path<?> path, Number value) {
        return new Gt(path, value);
    }

    /**
     * Greater than
     *
     * @param path  path
     * @param path2 path2
     * @return greater than for two paths
     */
    public Expression gt(Path<?> path, Path<?> path2) {
        return new Gt(path, path2);
    }

    /**
     * Lesser than a number
     *
     * @param path  path
     * @param value value
     * @return Lesser than a number
     */
    public Expression lt(Path<?> path, Number value) {
        return new Lt(path, value);
    }

    public <R> Expression lt(Path<R> date, Timestamp ts) {
        return new Lt(date, ts);
    }

    /**
     * In expression
     *
     * @param path   path
     * @param value  array of values
     * @param values values
     * @return in expression
     */
    public Expression in(Path<?> path, Object value, Object... values) {
        return new In(path, value, values);
    }

    /**
     * In with a nullable or empty collection of parameters
     * if the collection is empty, an IdentityExpression is returned
     *
     * @param path   path
     * @param values nullable in values
     * @return expression
     */
    public Expression inn(Path<?> path, List<?> values) {
        if (values == null || values.isEmpty()) {
            return new IdentityExpression();
        }
        return new In(path, values, null);
    }

    /**
     * Between two numbers
     *
     * @param path   path
     * @param value1 lower bound
     * @param value2 higher bound
     * @return between value1 and value2
     */
    public Expression between(Path<?> path, Number value1, Number value2) {
        return new Bt(path, value1, value2);
    }

    /**
     * Is not null expression
     *
     * @param path path
     * @return is not null
     */
    public Expression isNotNull(Path<?> path) {
        return new IsNotNull(path);
    }

    /**
     * Is null
     *
     * @param path path
     * @return is null
     */

    public Expression isNull(Path<?> path) {
        return new IsNull(path);
    }

    /**
     * And expression
     *
     * @param expression  expression
     * @param expressions other expressions
     * @return combined expressions with AND operator
     */
    public Expression and(Expression expression, Expression... expressions) {
        And and = new And(expression);
        expression.and.addAll(Arrays.asList(expressions));
        return and;
    }

    /**
     * Or expression
     *
     * @param expression  expression
     * @param expressions other expressions
     * @return combined expressions with OR operator
     */
    public Expression or(Expression expression, Expression... expressions) {
        Or or = new Or(expression);
        expression.or.addAll(Arrays.asList(expressions));
        return or;
    }

    /**
     * Max expression
     *
     * @param path attribute
     * @param <R>  type
     * @return max(attribute)
     */
    public <R> Path<R> max(Path<R> path) {
        return new Selection<>(path.head, path.attribute, new Max(new Selection<>(path)));
    }

    /**
     * Min expression
     *
     * @param path attribute
     * @param <R>  type
     * @return min(attribute)
     */
    public <R> Path<R> min(Path<R> path) {
        return new Selection<>(path.head, path.attribute, new Min(new Selection<>(path)));
    }

    /**
     * Min expression
     *
     * @param path attribute
     * @param <R>  type
     * @return min(attribute)
     */
    public <R> Path<R> toDate(Path<R> path) {
        return new Selection<>(path.head, path.attribute, new ToDate(new Selection<>(path)));
    }

    public <R> Path<R> toTimestamp(Path<R> path) {
        return new Selection<>(path.head, path.attribute, new ToTimestamp(new Selection<>(path)));
    }


    /**
     * Min expression
     *
     * @param path attribute
     * @param <R>  type
     * @return min(attribute)
     */
    public <R> Path<R> toDate(Path<R> path, String dateFormat) {
        return new Selection<>(path.head, path.attribute, new ToDate(new Selection<>(path), dateFormat));
    }

    public <R> Path<R> toTimestamp(Path<R> path, String dateFormat) {
        return new Selection<>(path.head, path.attribute, new ToTimestamp(new Selection<>(path), dateFormat));
    }

    /**
     * Sum expression
     *
     * @param path attribute
     * @param <R>  type
     * @return min(attribute)
     */
    public <R> Path<R> sum(Path<R> path) {
        return new Selection<>(path.head, path.attribute, new Sum(new Selection<>(path)));
    }

    /**
     * Avg expression
     *
     * @param path attribute
     * @param <R>  type
     * @return avg(attribute)
     */
    public <R> Path<R> avg(Path<R> path) {
        return new Selection<>(path.head, path.attribute, new Avg(new Selection<>(path)));
    }

    /**
     * Count expression
     *
     * @param path attribute
     * @param <R>  type
     * @return count(attribute)
     */
    public <R> Path<R> count(Path<R> path) {
        return new Selection<>(path.head, path.attribute, new Count(new Selection<>(path)));
    }

    /**
     * distinct expression
     *
     * @param path attribute
     * @param <R>  type
     * @return distinct(attribute)
     */
    public <R> Path<R> distinct(Path<R> path) {
        return new Selection<>(path.head, path.attribute, new Distinct(new Selection<>(path)));
    }

    /**
     * Count expression
     *
     * @param path root
     * @param <R>  type
     * @return count(attribute)
     */
    public <R> Path<R> count(Root<R> path) {
        return new Selection<>(path, null, new Count());
    }

    /**
     * Not expression
     *
     * @param expression expression to be negated
     * @return negated expression
     */
    public Expression not(Expression expression) {
        return new Not(expression);
    }


}
