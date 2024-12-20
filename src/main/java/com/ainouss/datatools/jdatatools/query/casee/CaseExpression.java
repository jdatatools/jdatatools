package com.ainouss.datatools.jdatatools.query.casee;


import com.ainouss.datatools.jdatatools.query.core.LiteralValue;
import com.ainouss.datatools.jdatatools.query.core.Selectable;
import com.ainouss.datatools.jdatatools.query.expression.SelectableExpression;

import java.util.ArrayList;
import java.util.List;

public class CaseExpression implements Selectable {

    private Selectable attribute;
    private final List<WhenThen> whenThens = new ArrayList<>();
    private Selectable otherwise;
    private String alias = "";

    public CaseExpression() {
    }


    public CaseExpression(Selectable attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("case ");
        if (attribute != null) {
            sql.append(attribute.toSql()).append(" "); // Searched CASE
        }

        for (WhenThen whenThen : whenThens) {
            sql.append("when ").append(whenThen.getWhen().toSql())
                    .append(" then ").append(whenThen.getThen().toSql()).append(" ");
        }

        if (otherwise != null) {
            sql.append("else ").append(otherwise.toSql()).append(" ");
        }

        sql.append("end");
        return sql.toString();
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    public When when(Object val) {
        SelectableExpression when = new SelectableExpression(new LiteralValue(val));
        WhenThen whenThen = new WhenThen(when);
        whenThens.add(whenThen);
        return new When(this, whenThen);
    }

    public CaseExpression otherwise(Object obj) {
        this.otherwise = new LiteralValue(obj);
        return this;
    }
}