package com.ainouss.datatools.jdatatools.query.expression;

import com.ainouss.datatools.jdatatools.query.core.Expression;

import java.util.Optional;

/**
 * Predicate expression : when(condition).then(expression1).otherwise(expression2)
 * depending on the value of condition, the predicate expression will be resolved to either expression1 or expression2
 * use end() to quit the predicate and return to the parent flow
 */
public class PredicateExpression {
    private final boolean predicate;
    private Expression expression;

    public PredicateExpression(boolean predicate) {
        this.predicate = predicate;
    }

    /**
     * Expression that is picked if predicate is true
     *
     * @param expression expression
     * @return this
     */
    public PredicateExpression then(Expression expression) {
        if (predicate) {
            this.expression = expression;
        }
        return this;
    }

    /**
     * Expression that is picked if predicate is false
     *
     * @param expression expression
     * @return this
     */
    public PredicateExpression otherwise(Expression expression) {
        if (!predicate) {
            this.expression = expression;
        }
        return this;
    }

    /**
     * end the predicate
     *
     * @return return the expression
     */
    public Expression end() {
        return Optional.ofNullable(expression).orElseGet(IdentityExpression::new);
    }
}
