package com.ainouss.datatools.jdatatools.query.core;

import lombok.EqualsAndHashCode;

/**
 * Selection of attributes, paths with or without aggregate functions
 *
 * @param <T> type
 */
@EqualsAndHashCode(callSuper = true)
public class Selection<T> extends Path<T> {

    protected final Expression expression;

    public Selection(Root<T> head, String attribute, Expression expression) {
        super(head, attribute);
        this.expression = expression;
    }

    /**
     * From path
     *
     * @param head      head
     * @param attribute attribute
     */
    public Selection(Root<T> head, String attribute) {
        super(head, attribute);
        this.expression = null;
    }

    /**
     * From path
     *
     * @param selection selection
     */

    public Selection(Path<T> selection) {
        super(selection.head, selection.attribute);
        if (selection instanceof Selection) {
            this.expression = ((Selection<?>) selection).expression;
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
}
