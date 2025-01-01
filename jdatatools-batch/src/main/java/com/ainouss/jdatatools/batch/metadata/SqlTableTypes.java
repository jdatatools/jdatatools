package com.ainouss.jdatatools.batch.metadata;


public enum SqlTableTypes {
    TABLE("TABLE"),
    VIEW("VIEW"),
    SYSTEM_TABLE("SYSTEM TABLE"),
    GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
    LOCAL_TEMPORARY("LOCAL TEMPORARY"),
    ALIAS("ALIAS"),
    SYNONYM("SYNONYM");

    private final String value;

    SqlTableTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static SqlTableTypes fromValue(String value) {
        for (SqlTableTypes type : SqlTableTypes.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null; // Or throw an IllegalArgumentException if the value is unexpected
    }
}