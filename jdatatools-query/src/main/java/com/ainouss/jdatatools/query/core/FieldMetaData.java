package com.ainouss.jdatatools.query.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldMetaData {

    private String label;
    private String column;
    private Class<?> javaType;

}
