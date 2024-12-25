package com.ainouss.jdatatools.query.choice;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;
import lombok.Data;

@Data
public class WhenThen {

    private final Expression when;
    private Selectable then;

    WhenThen(Expression when) {
        this.when = when;
    }


}