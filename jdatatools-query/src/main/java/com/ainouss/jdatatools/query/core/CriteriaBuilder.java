package com.ainouss.jdatatools.query.core;

import com.ainouss.jdatatools.query.choice.SearchedCase;
import com.ainouss.jdatatools.query.choice.SimpleCase;
import com.ainouss.jdatatools.query.dialect.SqlDialect;
import com.ainouss.jdatatools.query.dialect.StandardDialect;
import com.ainouss.jdatatools.query.function.*;
import com.ainouss.jdatatools.query.logical.AbstractExpression;
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
    private final SqlDialect sqlDialect;

    public CriteriaBuilder(SqlDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
    }

    public CriteriaBuilder() {
        this(new StandardDialect()); // Default to standard SQL if no dialect is specified
    }

    public SqlDialect getSqlDialect() {
        return sqlDialect;
    }

    /**
     * @param clazz class
     * @param <T>   type
     * @return new CriteriaQuery
     */
    public <T> CriteriaQuery<T> createQuery(Class<T> clazz) {
        return new CriteriaQuery<>(clazz, this);
    }

    public <T> ScalarQuery<T> scalar(CriteriaQuery<T> cr) {
        return new ScalarQuery<>(cr);
    }


    public Expression like(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Like(attribute, selectable, sqlDialect);
        }
        return new Like(attribute, new LiteralValue(value), sqlDialect);
    }

    public Expression endsWith(Selectable attribute, Object exp) {
        if (exp instanceof Selectable selectable) {
            return new EndsWith(attribute, selectable, sqlDialect);
        }
        return new EndsWith(attribute, new LiteralValue(exp), sqlDialect);
    }

    public Expression startsWith(Selectable attribute, Object exp) {
        if (exp instanceof Selectable selectable) {
            return new StartsWith(attribute, selectable, sqlDialect);
        }
        return new StartsWith(attribute, new LiteralValue(exp), sqlDialect);
    }

    public Expression eq(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Eq(attribute, selectable, sqlDialect);
        }
        return new Eq(attribute, new LiteralValue(value), sqlDialect);
    }


    public Expression ne(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Ne(attribute, selectable, sqlDialect);
        }
        return new Ne(attribute, new LiteralValue(value), sqlDialect);
    }

    public Expression gt(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Gt(attribute, selectable, sqlDialect);
        }
        return new Gt(attribute, new LiteralValue(value), sqlDialect);
    }


    public Expression lt(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Lt(attribute, selectable, sqlDialect);
        }
        return new Lt(attribute, new LiteralValue(value), sqlDialect);
    }

    public Expression le(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Le(attribute, selectable, sqlDialect);
        }
        return new Le(attribute, new LiteralValue(value), sqlDialect);
    }

    public Expression ge(Selectable attribute, Object value) {
        if (value instanceof Selectable selectable) {
            return new Ge(attribute, selectable, sqlDialect);
        }
        return new Ge(attribute, new LiteralValue(value), sqlDialect);
    }

    public Expression in(Selectable attribute, Object... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values must not be null or empty");
        }
        List<Selectable> list = Arrays.stream(values)
                .map(o -> o instanceof Selectable ? (Selectable) o : new LiteralValue(o))
                .toList();
        return new In(attribute, list, sqlDialect);
    }

    public Expression inL(Selectable attribute, List<?> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("values must not be null or empty");
        }
        List<Selectable> list = values.stream()
                .map(o -> o instanceof Selectable ? (Selectable) o : new LiteralValue(o))
                .toList();
        return new In(attribute, list, sqlDialect);
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
            return new EmptyExpression();
        }
        List<Selectable> list = values.stream()
                .map(o -> o instanceof Selectable ? (Selectable) o : new LiteralValue(o))
                .toList();
        return new In(attribute, list, sqlDialect);
    }

    public Expression between(Selectable attribute, Object exp1, Object exp2) {
        return new Bt(attribute, new LiteralValue(exp1), new LiteralValue(exp2), sqlDialect);
    }

    /**
     * Is not null expression
     *
     * @param path path
     * @return is not null
     */
    public Expression isNotNull(Path<?> path) {
        return new IsNotNull(path, sqlDialect);
    }

    /**
     * Is null
     *
     * @param path path
     * @return is null
     */

    public Expression isNull(Path<?> path) {
        return new IsNull(path, sqlDialect);
    }

    /**
     * And expression
     *
     * @param expression  expression
     * @param expressions other expressions
     * @return combined expressions with AND operator
     */
    public AbstractExpression and(Expression expression, Expression... expressions) {
        And and = new And(expression, sqlDialect);
        and.and(expressions);
        return and;
    }

    /**
     * Or expression
     *
     * @param expression  expression
     * @param expressions other expressions
     * @return combined expressions with OR operator
     */
    public AbstractExpression or(Expression expression, Expression... expressions) {
        Or or = new Or(expression, sqlDialect);
        or.or(expressions);
        return or;
    }

    /**
     * NOT expressions
     *
     * @param expressions other expressions
     * @return combined expressions with OR operator
     */
    public Expression not(Expression expression, Expression... expressions) {
        Expression and = new And(expression, sqlDialect).and(expressions);
        return new Not(and, sqlDialect);
    }

    /**
     * Max expression
     *
     * @return max(attribute)
     */
    public Selectable max(Selectable selectable) {
        return new Max(selectable, sqlDialect);
    }

    /**
     * Min expression
     *
     * @return min(attribute)
     */
    public Aggregable min(Selectable selectable) {
        return new Min(selectable, sqlDialect);
    }


    /**
     * Avg expression
     *
     * @param selectable attribute
     * @return avg(attribute)
     */
    public Aggregable sum(Selectable selectable) {
        return new Sum(selectable, sqlDialect);
    }

    public Aggregable rank() {
        return new Rank(sqlDialect);
    }

    public Aggregable rowNumber() {
        return new RowNumber(sqlDialect);
    }


    /**
     * Avg expression
     *
     * @param selectable attribute
     * @return avg(attribute)
     */
    public Aggregable avg(Selectable selectable) {
        return new Avg(selectable, sqlDialect);
    }

    /**
     * Count expression
     *
     * @return count(attribute)
     */
    public Aggregable count(Selectable selectable) {
        return new Count(selectable, sqlDialect);
    }

    /**
     * distinct expression
     *
     * @return distinct(attribute)
     */
    public Selectable distinct(Selectable selectable) {
        return new Distinct(selectable, sqlDialect);
    }


    /**
     * EXISTS subquery
     *
     * @param subquery subquery
     * @return EXISTS expression
     */
    public Expression exists(CriteriaQuery<?> subquery) {
        return new Exists(subquery, sqlDialect);
    }

    /**
     * ANY subquery
     *
     * @param subquery subquery
     * @return 'ANY' expression
     */
    public Expression any(CriteriaQuery<?> subquery) {
        return new Any(subquery, sqlDialect);
    }

    /**
     * ALL subquery
     *
     * @param subquery subquery
     * @return 'ALL' expression
     */
    public Expression all(CriteriaQuery<?> subquery) {
        return new All(subquery, sqlDialect);
    }


    public SimpleCase choice(Selectable attribute) {
        return new SimpleCase(attribute, sqlDialect);
    }

    public SearchedCase choice() {
        return new SearchedCase(sqlDialect);
    }

    public <T> Cte<T> with(String name) {
        return new Cte<>(name, sqlDialect);
    }
}