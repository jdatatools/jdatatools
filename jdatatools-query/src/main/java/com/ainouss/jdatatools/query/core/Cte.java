package com.ainouss.jdatatools.query.core;

public class Cte<T> extends CriteriaQuery<T> implements Source {

    private CriteriaQuery<?> cr;
    private final String name;
    private final SqlDialect sqlDialect;

    public Cte(String name, SqlDialect sqlDialect) {
        super(); // No Class<T> or CriteriaBuilder needed for CTE constructor
        this.name = name;
        this.sqlDialect = sqlDialect;
    }

    public Cte(String name) { // For CriteriaBuilder default constructor
        this(name, null);
    }


    public Cte<T> as(CriteriaQuery<?> cr) {
        this.cr = cr;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toSql() {
        return "with " + name + " as (" + cr.buildSelectQuery() + ")";
    }

    @Override
    public String buildSelectQuery() {
        String select = super.buildSelectQuery();
        return toSql() + " " + select;
    }

    @Override
    public Selectable get(String attr) {
        cr.checkSelection();
        return new Path<>(name, attr);
    }

    @Override
    public String getAlias() {
        return this.name;
    }

    @Override
    public void setAlias(String alias) {
        throw new UnsupportedOperationException("Alias is not supported for CTE");
    }
}