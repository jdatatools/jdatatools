package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.expression.IdentityExpression;
import com.ainouss.datatools.jdatatools.query.expression.PredicateExpression;
import com.ainouss.datatools.jdatatools.query.function.*;
import com.ainouss.datatools.jdatatools.query.operator.*;
import com.ainouss.datatools.jdatatools.query.subquery.All;
import com.ainouss.datatools.jdatatools.query.subquery.Any;
import com.ainouss.datatools.jdatatools.query.subquery.Exists;

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

    public <T> ScalarQuery<T> scalar(CriteriaQuery<T> cr) {
        return new ScalarQuery<>(cr);
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
     * @param path  path
     * @param value value
     * @return ne expression
     */
    public Expression ne(Path<?> path, Object value) {
        return new Ne(path, value);
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

    public Expression gt(Path<?> path, Expression expression) {
        return new Gt(path, expression);
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

    /**
     * Lesser than or equal a number
     *
     * @param path  path
     * @param value value
     * @return Lesser than a number
     */
    public Expression le(Path<?> path, Number value) {
        return new Le(path, value);
    }

    /**
     * Lesser than or equal a number
     *
     * @param path  path
     * @param value value
     * @return Lesser than a number
     */
    public Expression ge(Path<?> path, Number value) {
        return new Ge(path, value);
    }

    /**
     * Lesser than or equal a number
     *
     * @param path  path
     * @param value value
     * @return Lesser than a number
     */
    public Expression ge(Path<?> path, Expression value) {
        return new Ge(path, value);
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
        return new PathExpression<>(path.head, path.attribute, new Max(new PathExpression<>(path)));
    }

    /**
     * Min expression
     *
     * @param path attribute
     * @param <R>  type
     * @return min(attribute)
     */
    public <R> Path<R> min(Path<R> path) {
        return new PathExpression<>(path.head, path.attribute, new Min(new PathExpression<>(path)));
    }


    /**
     * Sum expression
     *
     * @param path attribute
     * @param <R>  type
     * @return min(attribute)
     */
    public <R> Path<R> sum(Path<R> path) {
        return new PathExpression<>(path.head, path.attribute, new Sum(new PathExpression<>(path)));
    }

    /**
     * Avg expression
     *
     * @param path attribute
     * @param <R>  type
     * @return avg(attribute)
     */
    public <R> Path<R> avg(Path<R> path) {
        return new PathExpression<>(path.head, path.attribute, new Avg(new PathExpression<>(path)));
    }

    /**
     * Count expression
     *
     * @param path attribute
     * @param <R>  type
     * @return count(attribute)
     */
    public <R> Path<R> count(Path<R> path) {
        return new PathExpression<>(path.head, path.attribute, new Count(new PathExpression<>(path)));
    }

    /**
     * distinct expression
     *
     * @param path attribute
     * @param <R>  type
     * @return distinct(attribute)
     */
    public <R> Path<R> distinct(Path<R> path) {
        return new PathExpression<>(path.head, path.attribute, new Distinct(new PathExpression<>(path)));
    }

    /**
     * Count expression
     *
     * @param path root
     * @param <R>  type
     * @return count(attribute)
     */
    public <R> Path<R> count(Root<R> path) {
        return new PathExpression<>(path, null, new Count());
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

    /**
     * NOT expressions
     *
     * @param expression  expression
     * @param expressions other expressions
     * @return combined expressions with OR operator
     */
    public Expression not(Expression expression, Expression... expressions) {
        Expression and = new IdentityExpression().and(expression, expressions);
        return new Not(and);
    }

    /**
     * EXISTS subquery
     *
     * @param subquery subquery
     * @return EXISTS expression
     */
    public Expression exists(CriteriaQuery<?> subquery) {
        return new Exists(subquery);
    }

    /**
     * ANY subquery
     *
     * @param subquery subquery
     * @return ANY expression
     */
    public Expression any(CriteriaQuery<?> subquery) {
        return new Any(subquery);
    }

    /**
     * ALL subquery
     *
     * @param subquery subquery
     * @return ALL expression
     */
    public Expression all(CriteriaQuery<?> subquery) {
        return new All(subquery);
    }


}
