package com.ainouss.jdatatools.query.core;

import com.ainouss.jdatatools.query.choice.SearchedCase;
import com.ainouss.jdatatools.query.choice.SimpleCase;
import com.ainouss.jdatatools.query.expression.IdentityExpression;
import com.ainouss.jdatatools.query.function.*;
import com.ainouss.jdatatools.query.logical.And;
import com.ainouss.jdatatools.query.logical.Not;
import com.ainouss.jdatatools.query.logical.Or;
import com.ainouss.jdatatools.query.operator.*;
import com.ainouss.jdatatools.query.subquery.All;
import com.ainouss.jdatatools.query.subquery.Any;
import com.ainouss.jdatatools.query.subquery.Exists;

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


    public Expression like(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Like(attribute, selectable);
        }
        return new Like(attribute, new LiteralValue(value));
    }

    public Expression endsWith(Selectable attribute, Object exp) {
        if (exp instanceof Selectable selectable) {
            return new EndsWith(attribute, selectable);
        }
        return new EndsWith(attribute, new LiteralValue(exp));
    }

    public Expression startsWith(Selectable attribute, Object exp) {
        if (exp instanceof Selectable selectable) {
            return new StartsWith(attribute, selectable);
        }
        return new StartsWith(attribute, new LiteralValue(exp));
    }

    public Expression eq(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Eq(attribute, selectable);
        }
        return new Eq(attribute, new LiteralValue(value));
    }


    public Expression ne(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Ne(attribute, selectable);
        }
        return new Ne(attribute, new LiteralValue(value));
    }

    public Expression gt(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Gt(attribute, selectable);
        }
        return new Gt(attribute, new LiteralValue(value));
    }


    public Expression lt(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Lt(attribute, selectable);
        }
        return new Lt(attribute, new LiteralValue(value));
    }

    public Expression le(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Le(attribute, selectable);
        }
        return new Le(attribute, new LiteralValue(value));
    }

    public Expression ge(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Ge(attribute, selectable);
        }
        return new Ge(attribute, new LiteralValue(value));
    }

    public Expression in(Selectable attribute, Object... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values must not be null or empty");
        }
        List<Selectable> list = Arrays.stream(values)
                .map(o -> o instanceof Selectable ? (Selectable) o : new LiteralValue(o))
                .toList();
        return new In(attribute, list);
    }

    public Expression inL(Selectable attribute, List<?> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("values must not be null or empty");
        }
        List<Selectable> list = values.stream()
                .map(o -> o instanceof Selectable ? (Selectable) o : new LiteralValue(o))
                .toList();
        return new In(attribute, list);
    }

    /**
     * In with a nullable or empty collection of parameters
     * if the collection is empty, an IdentityExpression is returned
     *
     * @param values nullable in values
     * @return expression
     */
    public Expression inn(Selectable attribute, List<?> values) {
        if (values == null || values.isEmpty()) {
            return new IdentityExpression();
        }
        List<Selectable> list = values.stream()
                .map(o -> o instanceof Selectable ? (Selectable) o : new LiteralValue(o))
                .toList();
        return new In(attribute, list);
    }

    public Expression between(Selectable attribute, Object exp1, Object exp2) {
        return new Bt(attribute, new LiteralValue(exp1), new LiteralValue(exp2));
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
     * @return max(attribute)
     */
    public Selectable max(Selectable selectable) {
        return new Max(selectable);
    }

    /**
     * Min expression
     *
     * @return min(attribute)
     */
    public Aggregable min(Selectable selectable) {
        return new Min(selectable);
    }


    /**
     * Avg expression
     *
     * @param selectable attribute
     * @return avg(attribute)
     */
    public Aggregable sum(Selectable selectable) {
        return new Sum(selectable);
    }

    public Aggregable rank() {
        return new Rank();
    }

    public Aggregable rowNumber() {
        return new RowNumber();
    }


    /**
     * Avg expression
     *
     * @param selectable attribute
     * @return avg(attribute)
     */
    public Aggregable avg(Selectable selectable) {
        return new Avg(selectable);
    }

    /**
     * Count expression
     *
     * @return count(attribute)
     */
    public Aggregable count(Selectable selectable) {
        return new Count(selectable);
    }

    /**
     * distinct expression
     *
     * @return distinct(attribute)
     */
    public Selectable distinct(Selectable selectable) {
        return new Distinct(selectable);
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
     * @param expressions other expressions
     * @return combined expressions with OR operator
     */
    public Expression not(Expression... expressions) {
        Expression and = new IdentityExpression().and(expressions);
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
     * @return 'ANY' expression
     */
    public Expression any(CriteriaQuery<?> subquery) {
        return new Any(subquery);
    }

    /**
     * ALL subquery
     *
     * @param subquery subquery
     * @return 'ALL' expression
     */
    public Expression all(CriteriaQuery<?> subquery) {
        return new All(subquery);
    }


    public SimpleCase choice(Selectable attribute) {
        return new SimpleCase(attribute);
    }

    public SearchedCase choice() {
        return new SearchedCase();
    }

    public <T> Cte<T> with(String name) {
        return new Cte<>(name);
    }


}
