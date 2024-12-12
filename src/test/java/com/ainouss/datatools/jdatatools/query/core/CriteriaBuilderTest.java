package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.model.Employee;
import com.ainouss.datatools.jdatatools.query.model.Profile;
import com.ainouss.datatools.jdatatools.query.registery.EntityRegistry;
import com.ainouss.datatools.jdatatools.query.registery.SampleEntityResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class CriteriaBuilderTest {

    private CriteriaBuilder cb;

    @BeforeEach
    public void setUp() {
        EntityRegistry entityRegistry = new EntityRegistry(new SampleEntityResolver(List.of(Employee.class, Profile.class)));
        cb = entityRegistry.getCriteriaBuilder();
    }

    @Test
    void createQuery() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        assertEquals("select EMPLOYEES.AGE as age,EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as name from EMPLOYEES EMPLOYEES", query.buildSelectQuery());
    }

    @Test
    void like() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.like(root.get("name"), "some name").sql();
        assertEquals(" like '%some name%'", sql);
    }

    @Test
    void endsWith() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.endsWith(root.get("name"), "doe").sql();
        assertEquals(" like '%doe'", sql);
    }

    @Test
    void startsWith() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.startsWith(root.get("name"), "john").sql();
        assertEquals(" like 'john%'", sql);
    }

    @Test
    void eq() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.eq(root.get("name"), "john").sql();
        assertEquals(" = 'john'", sql);
    }

    @Test
    void when__true_predicate() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setAge(66);
        Root<Employee> root = query.from();
        String sql = cb.when(employee.getAge() > 65)
                .then(
                        cb.like(root.get("name"), "senior")
                )
                .otherwise(
                        cb.like(root.get("name"), "junior")
                ).end()
                .sql();

        assertEquals(" like '%senior%'", sql);
    }

    @Test
    void when__otherwise_predicate() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setAge(25);
        Root<Employee> root = query.from();
        String sql = cb.when(employee.getAge() > 65)
                .then(
                        cb.like(root.get("name"), "senior")
                )
                .otherwise(
                        cb.like(root.get("name"), "junior")
                ).end()
                .sql();

        assertEquals(" like '%junior%'", sql);
    }

    @Test
    void gt() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setAge(25);
        Root<Employee> root = query.from();
        String sql = cb.gt(root.get("age"), employee.getAge()).sql();
        assertEquals(" > 25", sql);
    }

    @Test
    void lt() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setAge(25);
        Root<Employee> root = query.from();
        String sql = cb.lt(root.get("age"), employee.getAge()).sql();
        assertEquals(" < 25", sql);
    }

    @Test
    void in() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.in(root.get("name"), List.of("john","jane")).sql();
        assertEquals(" in  ('john','jane')", sql);
    }

    @Test
    void inn() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.inn(root.get("name"), List.of()).sql();
        assertEquals("", sql);
    }

    @Test
    void between() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.between(root.get("age"), 10,20).sql();
        assertEquals(" between 10 and 20", sql);
    }

    @Test
    void isNotNull() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.isNotNull(root.get("name")).sql();
        assertEquals(" is not null ", sql);
    }

    @Test
    void isNull() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.isNull(root.get("name")).sql();
        assertEquals(" is null ", sql);
    }

    @Test
    void and() {

    }

    @Test
    void or() {
    }

    @Test
    void max() {

    }

    @Test
    void min() {
    }

    @Test
    void toDate() {
    }

    @Test
    void sum() {
    }

    @Test
    void avg() {
    }

    @Test
    void count() {
    }

    @Test
    void distinct() {
    }


    @Test
    void not() {
    }
}