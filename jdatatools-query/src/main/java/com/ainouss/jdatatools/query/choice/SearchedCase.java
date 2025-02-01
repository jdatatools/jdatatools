package com.ainouss.jdatatools.query.choice;


import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.LiteralValue;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

public class SearchedCase extends Case {

    private final SqlDialect sqlDialect; // Dialect Integration

    public SearchedCase(SqlDialect sqlDialect) { // Dialect Integration
        super(sqlDialect); // Dialect Integration
        this.sqlDialect = sqlDialect;
    }

    public SearchedCase() { // For CTE - default constructor // Dialect Integration
        this(null);
    }


    public SearchedWhen when(Expression expression) {
        WhenThen whenThen = new WhenThen(expression);
        whenThens.add(whenThen);
        return new SearchedWhen(this, whenThen, sqlDialect); // Dialect Integration
    }

    public SearchedCase whenThen(Expression expression, Object then) {
        WhenThen whenThen = new WhenThen(expression);
        whenThen.setThen(new LiteralValue(then));
        whenThens.add(whenThen);
        return this;
    }

    public SearchedCase otherwise(Object obj) {
        this.otherwise = new LiteralValue(obj);
        return this;
    }
}