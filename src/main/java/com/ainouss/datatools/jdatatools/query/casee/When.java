package com.ainouss.datatools.jdatatools.query.casee;

import com.ainouss.datatools.jdatatools.query.core.LiteralValue;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

public class When {

    private final WhenThen whenThen;
    private final CaseExpression caseExpression;

    public When(CaseExpression caseExpression, WhenThen whenThen) {
        this.whenThen = whenThen;
        this.caseExpression = caseExpression;
    }

    public CaseExpression then(Object obj) {
        Selectable then = new LiteralValue(obj);
        whenThen.setThen(then);
        return caseExpression;
    }
}
