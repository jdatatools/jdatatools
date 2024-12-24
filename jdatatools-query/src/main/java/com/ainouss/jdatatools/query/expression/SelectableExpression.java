package com.ainouss.jdatatools.query.expression;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Identity expression, renders an empty SQL
 */
public class SelectableExpression extends Expression {

    private final Selectable selectable;

    public SelectableExpression(Selectable expression) {
        this.selectable = expression;
    }

    @Override
    public String sql() {
        return selectable.toSql();
    }
}
