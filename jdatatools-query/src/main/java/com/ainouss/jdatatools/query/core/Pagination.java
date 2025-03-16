package com.ainouss.jdatatools.query.core;

import lombok.Data;

@Data
public class Pagination {

    private Integer limit;
    private Integer offset;
    private final SqlDialect sqlDialect;

    public Pagination(SqlDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
    }

    public Pagination() {
        this(null); // For CTE - default constructor
    }

    public Pagination(Integer limit, Integer offset, SqlDialect sqlDialect) {
        this.limit = limit;
        this.offset = offset;
        this.sqlDialect = sqlDialect;
    }


    public static Pagination from(Pagination pagination) {
        return new Pagination(pagination.getLimit(), pagination.getOffset(), pagination.getSqlDialect());
    }

    public void apply(Pagination pagination) {
        this.limit = pagination.getLimit();
        this.offset = pagination.getOffset();
    }

    public boolean isVoid() {
        return limit == null && offset == null;
    }

    public String render() {
        if (this.isVoid() || sqlDialect == null) {
            return "";
        }
        return sqlDialect.getLimitOffsetSql(limit, offset);
    }

    public void clear() {
        this.limit = null;
        this.offset = null;
    }
}