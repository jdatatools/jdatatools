package com.ainouss.jdatatools.query.core;

import lombok.Data;

@Data
public class Pagination {

    private Integer limit;
    private Integer offset;

    public Pagination() {
    }

    public Pagination(Integer limit, Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public static Pagination from(Pagination pagination) {
        return new Pagination(pagination.getLimit(), pagination.getOffset());
    }

    public void apply(Pagination pagination) {
        this.limit = pagination.getLimit();
        this.offset = pagination.getOffset();
    }

    public boolean isVoid() {
        return limit == null && offset == null;
    }

    public String render() {
        if (this.isVoid()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (limit != null) {
            sb.append(" limit ").append(limit);
        }
        if (offset != null) {
            sb.append(" offset ").append(offset);
        }
        return sb.toString().trim();
    }

    public void clear() {
        this.limit = null;
        this.offset = null;
    }
}
