package com.ainouss.jdatatools.query.choice;

import com.ainouss.jdatatools.query.core.Alias;
import com.ainouss.jdatatools.query.core.Fragment;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

import java.util.ArrayList;
import java.util.List;

public abstract class Case extends Alias implements Selectable {

    protected Fragment attribute;
    protected final List<WhenThen> whenThens = new ArrayList<>();
    protected Fragment otherwise;
    private final SqlDialect sqlDialect; // Dialect Integration

    public Case(SqlDialect sqlDialect) { // Dialect Integration
        this.sqlDialect = sqlDialect;
    }

    public Case() { // For CTE - default constructor // Dialect Integration
        this(null);
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

    public Selectable end() {
        return this;
    }
}