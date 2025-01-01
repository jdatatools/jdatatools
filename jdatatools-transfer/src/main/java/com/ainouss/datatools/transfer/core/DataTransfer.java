package com.ainouss.datatools.transfer.core;


import com.ainouss.datatools.transfer.store.DataSourceRegistry;
import com.ainouss.jdatatools.query.core.CriteriaBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Modifier;

public class DataTransfer {

    private final JdbcTemplate jdbcTemplate;
    private final CriteriaBuilder cb = new CriteriaBuilder();

    public DataTransfer() {
        DataSource db1 = DataSourceRegistry.INSTANCE.getDataSource("db1");
        jdbcTemplate = new JdbcTemplate(db1);
    }

    public Class<?> register() {
        ByteBuddyAgent.install();
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .name("com.ainouss.datatools.transfer.model.Employee") // Choose a package and class name
                .annotateType(AnnotationDescription.Builder.ofType(Table.class)
                        .define("name", "EMPLOYEES")
                        .build())

                // Define fields with annotations
                .defineField("id", Long.class, Modifier.PRIVATE)
                .annotateField(AnnotationDescription.Builder.ofType(Column.class)
                        .define("name", "ID")
                        .build())
                .defineField("firstName", String.class, Modifier.PRIVATE)
                .annotateField(AnnotationDescription.Builder.ofType(Column.class)
                        .define("name", "FIRST_NAME")
                        .build())
                .defineField("lastName", String.class, Modifier.PRIVATE)
                .annotateField(AnnotationDescription.Builder.ofType(Column.class)
                        .define("name", "LAST_NAME")
                        .build())
                .defineField("salary", int.class, Modifier.PRIVATE)
                .annotateField(AnnotationDescription.Builder.ofType(Column.class)
                        .define("name", "SALARY")
                        .build())


                // Implement getters and setters for @Data functionality
                .defineMethod("getId", Long.class, Modifier.PUBLIC)
                .intercept(FieldAccessor.ofField("id"))
                .defineMethod("setId", void.class, Modifier.PUBLIC)
                .withParameter(Long.class, "id")
                .intercept(FieldAccessor.ofField("id"))

                .defineMethod("getFirstName", String.class, Modifier.PUBLIC)
                .intercept(FieldAccessor.ofField("firstName"))
                .defineMethod("setFirstName", void.class, Modifier.PUBLIC)
                .withParameter(String.class, "firstName")
                .intercept(FieldAccessor.ofField("firstName"))

                .defineMethod("getLastName", String.class, Modifier.PUBLIC)
                .intercept(FieldAccessor.ofField("lastName"))
                .defineMethod("setLastName", void.class, Modifier.PUBLIC)
                .withParameter(String.class, "lastName")
                .intercept(FieldAccessor.ofField("lastName"))

                .defineMethod("getSalary", int.class, Modifier.PUBLIC)
                .intercept(FieldAccessor.ofField("salary"))
                .defineMethod("setSalary", void.class, Modifier.PUBLIC)
                .withParameter(int.class, "salary")
                .intercept(FieldAccessor.ofField("salary"))

                .defineMethod("getEnabled", String.class, Modifier.PUBLIC)
                .intercept(FieldAccessor.ofField("enabled"))
                .defineMethod("setEnabled", void.class, Modifier.PUBLIC)
                .withParameter(String.class, "enabled")
                .intercept(FieldAccessor.ofField("enabled"))
                .make();
        return dynamicType.load(getClass().getClassLoader()).getLoaded();
    }


}
