package com.ainouss.jdatatools.query.core;

/**
 * Identity expression, renders an empty SQL
 */
public class SelectableExpression extends AbstractExpression {

    private final Selectable selectable;

    public SelectableExpression(Selectable expression) {
        this.selectable = expression;
    }

    @Override
    public String sql() {
        return selectable.toSql();
    }
}
