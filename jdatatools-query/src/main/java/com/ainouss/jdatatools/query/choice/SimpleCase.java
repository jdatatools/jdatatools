package com.ainouss.jdatatools.query.choice;


import com.ainouss.jdatatools.query.core.LiteralValue;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.expression.SelectableExpression;

public class SimpleCase extends Case {

    public SimpleCase(Selectable attribute) {
        this.attribute = attribute;
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