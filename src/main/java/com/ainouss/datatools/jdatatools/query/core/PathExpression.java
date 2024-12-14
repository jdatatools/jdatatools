package com.ainouss.datatools.jdatatools.query.core;

import lombok.EqualsAndHashCode;

/**
 * Selection of attributes, paths with or without aggregate functions
 *
 * @param <T> type
 */
@EqualsAndHashCode(callSuper = true)
public class PathExpression<T> extends Path<T> implements Projection<T> {

    protected final Expression expression;

    public PathExpression(Root<T> head, String attribute, Expression expression) {
        super(head, attribute);
        this.expression = expression;
    }

    /**
     * From path
     *
     * @param head      head
     * @param attribute attribute
     */
    public PathExpression(Root<T> head, String attribute) {
        super(head, attribute);
        this.expression = null;
    }

    /**
     * From path
     *
     * @param selection selection
     */

    public <P extends Path<T>> PathExpression(P selection) {
        super(selection.head, selection.attribute);
        if (selection instanceof PathExpression) {
            this.expression = ((PathExpression<?>) selection).expression;
        } else {
            this.expression = null;
        }
    }

    /**
     * to SQL
     *
     * @return SQL
     */
    @Override
    public String toString() {
        if (expression == null) {
            return super.toString();
        }
        return expression.sql();
    }

    public String render() {
        if (expression == null) {
            return super.toString();
        }
        return expression.render();
    }

    @Override
    public String output() {
        return render();
    }

    @Override
    public Root<T> head() {
        return head;
    }
}
