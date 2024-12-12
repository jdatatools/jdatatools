package com.ainouss.datatools.jdatatools.query.join;

import com.ainouss.datatools.jdatatools.query.core.Path;
import com.ainouss.datatools.jdatatools.query.core.Root;
import com.ainouss.datatools.jdatatools.query.core.Expression;
import lombok.Getter;

@Getter
public class Join<X, Y> extends Path<X> {

    private final Root<Y> target;

    private final JoinType joinType;

    private Expression on;

    public Join(Root<X> source, Root<Y> target, JoinType joinType) {
        super(source, null);
        this.target = target;
        this.joinType = joinType;
    }

    public Join<X, Y> on(Expression on) {
        this.on = on;
        return this;
    }
}
