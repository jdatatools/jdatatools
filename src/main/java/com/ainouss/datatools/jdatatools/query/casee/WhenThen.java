package com.ainouss.datatools.jdatatools.query.casee;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Selectable;
import lombok.Data;

@Data
public class WhenThen {

    private final Expression when;
    private Selectable then;

    WhenThen(Expression when) {
        this.when = when;
    }


}