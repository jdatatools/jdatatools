package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class CriteriaBuilderTest {

    private final CriteriaBuilder cb = new CriteriaBuilder();

    @Test
    void createQuery() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        assertEquals("select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES", query.buildSelectQuery());
    }

    @Test
    void like() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.like(root.get("lastName"), "some lastName").render();
        assertEquals("EMPLOYEES.LAST_NAME like '%some lastName%'", sql);
    }

    @Test
    void endsWith() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.endsWith(root.get("lastName"), "doe").render();
        assertEquals("EMPLOYEES.LAST_NAME like '%doe'", sql);
    }

    @Test
    void startsWith() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.startsWith(root.get("lastName"), "john").render();
        assertEquals("EMPLOYEES.LAST_NAME like 'john%'", sql);
    }

    @Test
    void eq() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.eq(root.get("lastName"), "john").render();
        assertEquals("EMPLOYEES.LAST_NAME = 'john'", sql);
    }

    @Test
    void when__true_predicate() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(66);
        Root<Employee> root = query.from();
        String sql = cb.when(employee.getSalary() > 65)
                .then(
                        cb.like(root.get("lastName"), "senior")
                )
                .otherwise(
                        cb.like(root.get("lastName"), "junior")
                ).end()
                .render();

        assertEquals("EMPLOYEES.LAST_NAME like '%senior%'", sql);
    }

    @Test
    void when__otherwise_predicate() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(25);
        Root<Employee> root = query.from();
        String sql = cb.when(employee.getSalary() > 65)
                .then(
                        cb.like(root.get("lastName"), "senior")
                )
                .otherwise(
                        cb.like(root.get("lastName"), "junior")
                ).end()
                .render();

        assertEquals("EMPLOYEES.LAST_NAME like '%junior%'", sql);
    }

    @Test
    void gt() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(25);
        Root<Employee> root = query.from();
        String sql = cb.gt(root.get("salary"), employee.getSalary()).render();
        assertEquals("EMPLOYEES.SALARY > 25", sql);
    }

    @Test
    void lt() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(25);
        Root<Employee> root = query.from();
        String sql = cb.lt(root.get("salary"), employee.getSalary()).render();
        assertEquals("EMPLOYEES.SALARY < 25", sql);
    }

    @Test
    void le() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(25);
        Root<Employee> root = query.from();
        String sql = cb.le(root.get("salary"), employee.getSalary()).render();
        assertEquals("EMPLOYEES.SALARY <= 25", sql);
    }

    @Test
    void ge() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(25);
        Root<Employee> root = query.from();
        String sql = cb.ge(root.get("salary"), employee.getSalary()).render();
        assertEquals("EMPLOYEES.SALARY >= 25", sql);
    }

    @Test
    void in() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.in(root.get("lastName"), List.of("john", "jane")).render();
        assertEquals("EMPLOYEES.LAST_NAME in  ('john','jane')", sql);
    }

    @Test
    void inn() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.inn(root.get("lastName"), List.of()).render();
        assertEquals("", sql);
    }

    @Test
    void between() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.between(root.get("salary"), 10, 20).render();
        assertEquals("EMPLOYEES.SALARY between 10 and 20", sql);
    }

    @Test
    void isNotNull() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.isNotNull(root.get("lastName")).render();
        assertEquals("EMPLOYEES.LAST_NAME is not null ", sql);
    }

    @Test
    void isNull() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.isNull(root.get("lastName")).render();
        assertEquals("EMPLOYEES.LAST_NAME is null ", sql);
    }

    @Test
    void and() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.and(
                cb.eq(root.get("firstName"), "John"),
                cb.eq(root.get("lastName"), "doe")
        ).render();
        assertEquals("(EMPLOYEES.FIRST_NAME = 'John' and (EMPLOYEES.LAST_NAME = 'doe'))", sql);
    }

    @Test
    void or() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.or(
                cb.eq(root.get("firstName"), "John"),
                cb.eq(root.get("lastName"), "doe")
        ).render();
        assertEquals("(EMPLOYEES.FIRST_NAME = 'John' or (EMPLOYEES.LAST_NAME = 'doe'))", sql);
    }

    @Test
    void not() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.not(
                cb.eq(root.get("firstName"), "John"),
                cb.eq(root.get("lastName"), "doe")
        ).render();
        assertEquals(" NOT ((EMPLOYEES.FIRST_NAME = 'John' and EMPLOYEES.LAST_NAME = 'doe'))", sql);
    }

    @Test
    void max() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.max(root.get("salary")).render();
        assertEquals("max(EMPLOYEES.SALARY)", sql);
    }

    @Test
    void min() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.min(root.get("salary")).render();
        assertEquals("min(EMPLOYEES.SALARY)", sql);
    }


    @Test
    void sum() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.sum(root.get("salary")).render();
        assertEquals("sum(EMPLOYEES.SALARY)", sql);
    }

    @Test
    void avg() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.avg(root.get("salary")).render();
        assertEquals("avg(EMPLOYEES.SALARY)", sql);
    }

    @Test
    void count() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.count(root.get("salary")).render();
        assertEquals("count(EMPLOYEES.SALARY)", sql);
    }

    @Test
    void distinct() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.distinct(root.get("salary")).render();
        assertEquals("distinct EMPLOYEES.SALARY", sql);
    }

    @Test
    void ne() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        String sql = cb.ne(root.get("name"), "John Doe").render();
        assertEquals("EMPLOYEES.name != 'John Doe'", sql);
    }
    @Test
    void aggregate_function(){
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");
        String sql = cb.gt(cb.sum(emp.get("salary")), 1000L).render();
        assertEquals("sum(tbl.SALARY) > 1000", sql);
    }


}