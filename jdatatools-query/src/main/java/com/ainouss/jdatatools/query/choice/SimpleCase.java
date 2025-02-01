package com.ainouss.jdatatools.query.choice;


import com.ainouss.jdatatools.query.core.LiteralValue;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.core.SelectableExpression;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

public class SimpleCase extends Case {

    private final SqlDialect sqlDialect; // Dialect Integration

    public SimpleCase(Selectable attribute, SqlDialect sqlDialect) { // Dialect Integration
        super(sqlDialect); // Dialect Integration
        this.attribute = attribute;
        this.sqlDialect = sqlDialect;
    }

    public SimpleCase() { // For CTE - default constructor // Dialect Integration
        this(null, null);
    }
    public SimpleCase(SqlDialect sqlDialect) { // For CriteriaBuilder default constructor // Dialect Integration
        this(null, sqlDialect);
    }


    public SimpleWhen when(Object val) {
        SelectableExpression when = new SelectableExpression(new LiteralValue(val));
        WhenThen whenThen = new WhenThen(when);
        whenThens.add(whenThen);
        return new SimpleWhen(this, whenThen);
    }

    public SimpleCase whenThen(Object when, Object obj) {
        WhenThen whenThen = new WhenThen(new SelectableExpression(new LiteralValue(when)));
        whenThen.setThen(new LiteralValue(obj));
        whenThens.add(whenThen);
        return this;
    }

    public SimpleCase otherwise(Object obj) {
        this.otherwise = new LiteralValue(obj);
        return this;
    }
}