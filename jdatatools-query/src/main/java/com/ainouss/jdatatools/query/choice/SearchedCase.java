package com.ainouss.jdatatools.query.choice;


import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.LiteralValue;

public class SearchedCase extends Case {

    public SearchedCase() {
    }

    public SearchedWhen when(Expression expression) {
        WhenThen whenThen = new WhenThen(expression);
        whenThens.add(whenThen);
        return new SearchedWhen(this, whenThen);
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