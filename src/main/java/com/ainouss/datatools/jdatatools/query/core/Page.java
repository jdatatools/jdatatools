package com.ainouss.datatools.jdatatools.query.core;

import lombok.Data;

@Data
public class Page {

    private Integer limit;
    private Integer offset;

    public Page() {
    }

    public Page(Integer limit, Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public static Page from(Page page) {
        return new Page(page.getLimit(), page.offset);
    }
    public  Page apply(Page page) {
        this.limit = page.getLimit();
        this.offset = page.getOffset();
        return this;
    }
    public Page limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Page offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public boolean isEmpty() {
        return limit == null && offset == null;
    }

    public String render() {
        if (this.isEmpty()) {
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
