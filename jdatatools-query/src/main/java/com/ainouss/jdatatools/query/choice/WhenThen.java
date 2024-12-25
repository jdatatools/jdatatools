package com.ainouss.jdatatools.query.choice;

import com.ainouss.jdatatools.query.core.AbstractExpression;
import com.ainouss.jdatatools.query.core.Selectable;
import lombok.Data;

@Data
public class WhenThen {

    private final AbstractExpression when;
    private Selectable then;

    WhenThen(AbstractExpression when) {
        this.when = when;
    }


}