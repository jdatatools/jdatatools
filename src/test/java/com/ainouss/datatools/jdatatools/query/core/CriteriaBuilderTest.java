package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.model.Employee;
import com.ainouss.datatools.jdatatools.query.model.EmployeeDetails;
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
        Root<Employee> root = query.from(Employee.class);
        String actual = query
                .select(root)
                .buildSelectQuery();
        assertEquals("select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES", actual);
    }

    @Test
    void like() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.like(root.get("lastName"), "some lastName").render();
        assertEquals("EMPLOYEES.LAST_NAME like '%some lastName%'", sql);
    }

    @Test
    void endsWith() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.endsWith(root.get("lastName"), "doe").render();
        assertEquals("EMPLOYEES.LAST_NAME like '%doe'", sql);
    }

    @Test
    void startsWith() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.startsWith(root.get("lastName"), "john").render();
        assertEquals("EMPLOYEES.LAST_NAME like 'john%'", sql);
    }

    @Test
    void eq() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.eq(root.get("lastName"), "john").render();
        assertEquals("EMPLOYEES.LAST_NAME = 'john'", sql);
    }


    @Test
    void gt() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(25);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.gt(root.get("salary"), employee.getSalary()).toSql();
        assertEquals("EMPLOYEES.SALARY > 25", sql);
    }

    @Test
    void lt() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(25);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.lt(root.get("salary"), employee.getSalary()).toSql();
        assertEquals("EMPLOYEES.SALARY < 25", sql);
    }

    @Test
    void le() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(25);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.le(root.get("salary"), employee.getSalary()).toSql();
        assertEquals("EMPLOYEES.SALARY <= 25", sql);
    }

    @Test
    void ge() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Employee employee = new Employee();
        employee.setSalary(25);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.ge(root.get("salary"), employee.getSalary()).toSql();
        assertEquals("EMPLOYEES.SALARY >= 25", sql);
    }

    @Test
    void inL() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.inL(root.get("lastName"), List.of("john", "jane")).toSql();
        assertEquals("EMPLOYEES.LAST_NAME in  ('john','jane')", sql);
    }

    @Test
    void in() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.in(root.get("lastName"), "john", "jane").toSql();
        assertEquals("EMPLOYEES.LAST_NAME in  ('john','jane')", sql);
    }

    @Test
    void inn() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.inn(root.get("lastName"), List.of()).toSql();
        assertEquals("", sql);
    }

    @Test
    void between() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.between(root.get("salary"), 10, 20).toSql();
        assertEquals("EMPLOYEES.SALARY between 10 and 20", sql);
    }

    @Test
    void isNotNull() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.isNotNull(root.get("lastName")).toSql();
        assertEquals("EMPLOYEES.LAST_NAME is not null ", sql);
    }

    @Test
    void isNull() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.isNull(root.get("lastName")).toSql();
        assertEquals("EMPLOYEES.LAST_NAME is null ", sql);
    }

    @Test
    void and() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.and(
                cb.eq(root.get("firstName"), "John"),
                cb.eq(root.get("lastName"), "doe")
        ).render();
        assertEquals("(EMPLOYEES.FIRST_NAME = 'John' and (EMPLOYEES.LAST_NAME = 'doe'))", sql);
    }

    @Test
    void or() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.or(
                cb.eq(root.get("firstName"), "John"),
                cb.eq(root.get("lastName"), "doe")
        ).render();
        assertEquals("(EMPLOYEES.FIRST_NAME = 'John' or (EMPLOYEES.LAST_NAME = 'doe'))", sql);
    }

    @Test
    void not() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.not(
                cb.eq(root.get("firstName"), "John"),
                cb.eq(root.get("lastName"), "doe")
        ).render();
        assertEquals(" NOT ((EMPLOYEES.FIRST_NAME = 'John' and EMPLOYEES.LAST_NAME = 'doe'))", sql);
    }

    @Test
    void max() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.max(root.get("salary")).toSql();
        assertEquals("max(EMPLOYEES.SALARY)", sql);
    }

    @Test
    void min() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.min(root.get("salary")).toSql();
        assertEquals("min(EMPLOYEES.SALARY)", sql);
    }


    @Test
    void sum() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.sum(root.get("salary")).toSql();
        assertEquals("sum(EMPLOYEES.SALARY)", sql);
    }

    @Test
    void avg() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.avg(root.get("salary")).toSql();
        assertEquals("avg(EMPLOYEES.SALARY)", sql);
    }

    @Test
    void count() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.count(root.get("salary")).toSql();
        assertEquals("count(EMPLOYEES.SALARY)", sql);
    }

    @Test
    void distinct() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.distinct(root.get("salary")).toSql();
        assertEquals("distinct EMPLOYEES.SALARY", sql);
    }

    @Test
    void ne() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        String sql = cb.ne(root.get("name"), "John Doe").toSql();
        assertEquals("EMPLOYEES.name != 'John Doe'", sql);
    }

    @Test
    void aggregate_function() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");
        String sql = cb.gt(cb.sum(emp.get("salary")), 1000L).toSql();
        assertEquals("sum(tbl.SALARY) > 1000", sql);
    }

    @Test
    void simple_case() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");
        String sql =
                cb.choice(emp.get("salary"))
                        .when(100)
                        .then("slave")
                        .when(200)
                        .then("employee")
                        .otherwise("rich")
                        .as("status")
                        .toSql();
        assertEquals("case tbl.SALARY when 100 then 'slave' when 200 then 'employee' else 'rich' end", sql);
    }

    @Test
    void searched_case() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");
        String sql =
                cb.choice()
                        .when(cb.eq(emp.get("salary"), 100))
                        .then("slave")
                        .when(cb.eq(emp.get("salary"), 200))
                        .then("employee")
                        .otherwise("rich")
                        .as("status")
                        .toSql();
        assertEquals("case when tbl.SALARY = 100 then 'slave' when tbl.SALARY = 200 then 'employee' else 'rich' end", sql);
    }

    @Test
    void nested_case() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class);
        String sql = cb.choice()
                .when(cb.gt(emp.get("age"), 60))
                .then("Retired")
                .otherwise(
                        cb.choice(emp.get("departmentId"))  // Inner Simple CASE
                                .whenThen(1, "Sales")
                                .whenThen(2, "Marketing")
                                .otherwise("Other")

                ).end().as("status").toSql();
        assertEquals("case when EMPLOYEES.age > 60 then 'Retired' else case EMPLOYEES.departmentId when 1 then 'Sales' when 2 then 'Marketing' else 'Other' end end", sql);
    }

    @Test
    void with_cte() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> root = cr.from(Employee.class);
        String sql = cb.with("my_query").as(
                cr.select()
                        .from(root)
        ).toSql();
        assertEquals("with my_query as (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES)", sql);
    }

    @Test
    public void test_joins() {
        CriteriaQuery<Employee> q1 = cb.createQuery(Employee.class);
        Root<Employee> emp = q1.from(Employee.class).as("emp");

        CriteriaQuery<EmployeeDetails> q2 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> det = q2.from(EmployeeDetails.class).as("det");

        String sql = emp.leftJoin(det).on(cb.eq(emp.get("id"), det.get("id")))
                .rightJoin(det).on(cb.eq(emp.get("id"), det.get("id"))).toSql();
        assertEquals("EMPLOYEES emp left join EMPLOYEE_DETAILS det on emp.ID = det.ID  right join EMPLOYEE_DETAILS det on emp.ID = det.ID", sql);
    }
}