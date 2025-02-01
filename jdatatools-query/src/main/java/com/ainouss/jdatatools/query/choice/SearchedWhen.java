package com.ainouss.jdatatools.query.choice;

import com.ainouss.jdatatools.query.core.LiteralValue;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

public class SearchedWhen {

    private final WhenThen whenThen;
    private final SearchedCase caseExpression;
    private final SqlDialect sqlDialect; // Dialect Integration

    public SearchedWhen(SearchedCase choice, WhenThen whenThen, SqlDialect sqlDialect) { // Dialect Integration
        this.whenThen = whenThen;
        this.caseExpression = choice;
        this.sqlDialect = sqlDialect;
    }

    public SearchedCase then(Object obj) {
        Selectable then = new LiteralValue(obj);
        whenThen.setThen(then);
        return caseExpression;
    }
}