package com.ainouss.datatools.jdatatools.query.casee;

import com.ainouss.datatools.jdatatools.query.core.Alias;
import com.ainouss.datatools.jdatatools.query.core.Fragment;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

import java.util.ArrayList;
import java.util.List;

public abstract class Choice extends Alias implements Selectable {

    protected Fragment attribute;
    protected final List<WhenThen> whenThens = new ArrayList<>();
    protected Fragment otherwise;

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

    public Selectable end() {
        return this;
    }
}
