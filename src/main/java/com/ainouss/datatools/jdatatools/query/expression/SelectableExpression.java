package com.ainouss.datatools.jdatatools.query.expression;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

/**
 * Identity expression, renders an empty SQL
 */
public class SelectableExpression extends Expression {

    private final Selectable selectable;

    public SelectableExpression(Selectable expression) {
        this.selectable = expression;
    }

    @Override
    public String toSql() {
        return selectable.toSql();
    }
}
