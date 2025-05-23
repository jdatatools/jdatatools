package com.ainouss.jdatatools.query.choice;

import com.ainouss.jdatatools.query.core.LiteralValue;
import com.ainouss.jdatatools.query.core.Selectable;

public class SimpleWhen {

    private final WhenThen whenThen;
    private final SimpleCase caseExpression;

    public SimpleWhen(SimpleCase caseExpression, WhenThen whenThen) {
        this.whenThen = whenThen;
        this.caseExpression = caseExpression;
    }

    public SimpleCase then(Object obj) {
        Selectable then = new LiteralValue(obj);
        whenThen.setThen(then);
        return caseExpression;
    }
}