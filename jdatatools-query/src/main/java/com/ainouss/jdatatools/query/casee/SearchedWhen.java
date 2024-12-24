package com.ainouss.jdatatools.query.casee;

import com.ainouss.jdatatools.query.core.LiteralValue;
import com.ainouss.jdatatools.query.core.Selectable;

public class SearchedWhen {

    private final WhenThen whenThen;
    private final SearchedCase caseExpression;

    public SearchedWhen(SearchedCase choice, WhenThen whenThen) {
        this.whenThen = whenThen;
        this.caseExpression = choice;
    }

    public SearchedCase then(Object obj) {
        Selectable then = new LiteralValue(obj);
        whenThen.setThen(then);
        return caseExpression;
    }
}
