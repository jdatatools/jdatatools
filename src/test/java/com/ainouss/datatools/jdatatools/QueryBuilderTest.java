package com.ainouss.datatools.jdatatools;

import com.ainouss.datatools.jdatatools.query.CriteriaBuilder;
import com.ainouss.datatools.jdatatools.query.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.EntityRegistry;
import com.ainouss.datatools.jdatatools.query.SampleEntityResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ExtendWith(SpringExtension.class)
class QueryBuilderTest {

    private CriteriaBuilder cb;

    @BeforeEach
    public void setUp() {
        EntityRegistry entityRegistry = new EntityRegistry(new SampleEntityResolver(List.of(Employee.class, Profile.class)));
        cb = entityRegistry.getCriteriaBuilder();
    }

    @Test
    public void should_generate_insert_one_field() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        var rt = cr.from();
        cr.select(rt.get("id"));
        String insert = cr.buildInsertQuery();
        Assertions.assertEquals("insert into EMPLOYEES (ID) values (:id)", insert);
    }

    @Test
    public void should_generate_insert_multiple_fields() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        var rt = cr.from();
        cr.select(rt.get("id"), rt.get("firstName"));
        String insert = cr.buildInsertQuery();
        Assertions.assertEquals("insert into EMPLOYEES (ID,FIRST_NAME) values (:id,:firstName)", insert);
    }

    @Test
    public void should_generate_insert_all_fields() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        String insert = cr.buildInsertQuery();
        Assertions.assertEquals("insert into EMPLOYEES (AGE,ENABLED,FIRST_NAME,ID,LAST_NAME) values (:age,:enabled,:firstName,:id,:name)", insert);
    }

}