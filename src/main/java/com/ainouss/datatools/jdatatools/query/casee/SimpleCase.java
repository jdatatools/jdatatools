package com.ainouss.datatools.jdatatools.query.casee;


import com.ainouss.datatools.jdatatools.query.core.LiteralValue;
import com.ainouss.datatools.jdatatools.query.core.Selectable;
import com.ainouss.datatools.jdatatools.query.expression.SelectableExpression;

public class SimpleCase extends Choice {

    public SimpleCase(Selectable attribute) {
        this.attribute = attribute;
    }

    public SimpleWhen when(Object val) {
        SelectableExpression when = new SelectableExpression(new LiteralValue(val));
        WhenThen whenThen = new WhenThen(when);
        whenThens.add(whenThen);
        return new SimpleWhen(this, whenThen);
    }

    public SimpleCase when(Object when, Object obj) {
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