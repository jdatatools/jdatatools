package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.model.Department;
import com.ainouss.datatools.jdatatools.query.model.Employee;
import com.ainouss.datatools.jdatatools.query.model.EmployeeDetails;
import com.ainouss.datatools.jdatatools.query.order.OrderDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
class CriteriaQueryTest {

    private final CriteriaBuilder cb = new CriteriaBuilder();


    @Test
    public void should__simple_query() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");

        cr.select(rt.get("id"), rt.get("lastName"))
                .from(rt)
                .where(cb.gt(rt.get("id"), 0)
                        .and(
                                cb.lt(rt.get("id"), 30),
                                cb.gt(rt.get("id"), 10),
                                cb.lt(rt.get("id"), 20),
                                cb.gt(rt.get("id"), 17),
                                cb.lt(rt.get("id"), 19)
                        ));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id,tbl.LAST_NAME as lastName from EMPLOYEES tbl where (tbl.ID > 0 and (tbl.ID < 30 and tbl.ID > 10 and tbl.ID < 20 and tbl.ID > 17 and tbl.ID < 19))", select);
    }

    @Test
    public void should__complex_criteria() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr
                .select(rt)
                .where(
                        cb.eq(
                                        rt.get("firstName"), "XYZ"
                                )
                                .or(
                                        cb.eq(
                                                rt.get("firstName"), "ABCD"
                                        ).and(
                                                cb.eq(rt.get("id"), 1000)
                                        )
                                )
                                .or(
                                        cb.eq(rt.get("firstName"), "ABBFSTO")
                                                .and(
                                                        cb.eq(rt.get("id"), 15)
                                                                .or(
                                                                        cb.between(rt.get("id"), 5, 1000)
                                                                )
                                                )

                                )
                                .and(cb.like(rt.get("salary"), "TEST"))
                                .and(cb.isNull(rt.get("lastName")))
                                .and(cb.isNotNull(rt.get("enabled"))));
        String select = cr
                .select(rt)
                .buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl where (tbl.FIRST_NAME = 'XYZ' and (tbl.SALARY like '%TEST%' and tbl.LAST_NAME is null and tbl.ENABLED is not null ) or (tbl.FIRST_NAME = 'ABCD' and (tbl.ID = 1000) or tbl.FIRST_NAME = 'ABBFSTO' and (tbl.ID = 15 or (tbl.ID between 5 and 1000))))", select);
    }

    @Test
    public void should__select_with_complex_where() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr
                .select(rt)
                .where(
                        cb.and(
                                cb.gt(rt.get("id"), 10)
                                        .and(
                                                cb.lt(rt.get("id"), 20)
                                        ),
                                cb.gt(rt.get("id"), 10)
                                        .and(
                                                cb.lt(rt.get("id"), 1000)
                                        )
                        ).or(
                                cb.gt(rt.get("id"), 55)
                                        .and(
                                                cb.lt(rt.get("id"), 99)
                                        )
                        )
                );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl where ((tbl.ID > 10 and (tbl.ID < 20 and tbl.ID > 10 and (tbl.ID < 1000))) or (tbl.ID > 55 and (tbl.ID < 99)))", select);
    }


    @Test
    public void should__gt_with_expression() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(
                rt.get("id"),
                rt.get("firstName")
        ).where(
                cb.gt(rt.get("id"), rt.get("firstName"))
        );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.FIRST_NAME as firstName,tbl.ID as id from EMPLOYEES tbl where (tbl.ID > tbl.FIRST_NAME)", select);
    }

    @Test
    public void should__order_by() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        var query = cr.select(
                rt.get("id"),
                rt.get("firstName")
        ).orderBy(
                rt.get("id"), OrderDirection.ASC
        );
        String select = query.buildSelectQuery();
        Assertions.assertEquals("select tbl.FIRST_NAME as firstName,tbl.ID as id from EMPLOYEES tbl order by tbl.ID ASC", select);
    }

    @Test
    public void should__order_by_asc() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        var query = cr.select(
                rt.get("id"),
                rt.get("firstName")
        ).orderBy(
                rt.get("id").asc()
        );
        String select = query.buildSelectQuery();
        Assertions.assertEquals("select tbl.FIRST_NAME as firstName,tbl.ID as id from EMPLOYEES tbl order by tbl.ID ASC", select);
    }

    @Test
    public void should__order_by_des() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        var query = cr.select(
                rt.get("id"),
                rt.get("firstName")
        ).orderBy(
                rt.get("id").desc()
        );
        String select = query.buildSelectQuery();
        Assertions.assertEquals("select tbl.FIRST_NAME as firstName,tbl.ID as id from EMPLOYEES tbl order by tbl.ID ASC", select);
    }

    @Test
    public void should__eq_with_expression() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(
                rt.get("id"),
                rt.get("firstName")
        ).where(
                cb.eq(rt.get("id"), rt.get("firstName"))
        );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.FIRST_NAME as firstName,tbl.ID as id from EMPLOYEES tbl where (tbl.ID = tbl.FIRST_NAME)", select);
    }

    @Test
    public void should__left_join() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Root<EmployeeDetails> det = cr.from(EmployeeDetails.class).as("det");
        Expression where = cb.and(
                cb.eq(det.get("id"), 0),
                cb.gt(rt.get("id"), 1)
        );
        cr
                .select(rt)
                .from(rt.leftJoin(det)
                        .on(
                                cb.eq(rt.get("id"), det.get("id"))
                        ))
                .where(where);
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl left join EMPLOYEE_DETAILS det on tbl.ID = det.ID where ((det.ID = 0 and (tbl.ID > 1)))", select);
    }

    @Test
    public void should__left_join_with_schema() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class)
                .schema("DB")
                .as("tbl");
        Root<EmployeeDetails> det = cr.from(EmployeeDetails.class)
                .schema("DB2")
                .as("det");

        Expression and = cb.and(
                cb.eq(det.get("id"), 0),
                cb.gt(rt.get("id"), 1)
        );
        cr
                .select(rt)
                .from(rt.leftJoin(det)
                        .on(
                                cb.eq(rt.get("id"), det.get("id"))
                        )).where(and);
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from DB.EMPLOYEES tbl left join DB2.EMPLOYEE_DETAILS det on tbl.ID = det.ID where ((det.ID = 0 and (tbl.ID > 1)))", select);
    }

    @Test
    void testSubqueryInJoinCondition() {

        CriteriaBuilder cb = new CriteriaBuilder();

        CriteriaQuery<Employee> employeeQuery = cb.createQuery(Employee.class);
        Root<Employee> employee = employeeQuery.from(Employee.class).as("e");

        Root<Department> department = employeeQuery.from(Department.class).as("d");

        CriteriaQuery<EmployeeDetails> subquery = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> details = subquery.from(EmployeeDetails.class);

        subquery.select(details.get("departmentId")) // Select department_id
                .where(cb.eq(details.get("employeeId"), employee.get("id")));  // Correlate with outer query


        employeeQuery.select(employee.get("lastName"), department.get("name"))
                .from(employee.join(department).on(subquery));// Select names

        String sql = employeeQuery.buildSelectQuery();
        assertEquals("select e.LAST_NAME as lastName,d.NAME as name from EMPLOYEES e cross join DEPARTMENTS d on (select EMPLOYEE_DETAILS.DEPARTMENT_ID as departmentId from EMPLOYEE_DETAILS EMPLOYEE_DETAILS where (EMPLOYEE_DETAILS.EMPLOYEE_ID = e.ID))", sql);
    }

    @Test
    public void should__select_max() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.max(rt.get("id")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select max(tbl.ID) from EMPLOYEES tbl", select);
    }

    @Test
    public void should__select_min() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.min(rt.get("id")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select min(tbl.ID) from EMPLOYEES tbl", select);
    }


    @Test
    public void should__select_sum() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.sum(rt.get("id")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select sum(tbl.ID) from EMPLOYEES tbl", select);
    }

    @Test
    public void should__select_avg() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.avg(rt.get("id")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select avg(tbl.ID) from EMPLOYEES tbl", select);
    }

    @Test
    public void should__full_join() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Root<EmployeeDetails> det = cr.from(EmployeeDetails.class).as("det");

        cr.select(rt.get("id"))
                .from(rt.fullJoin(det)
                        .on(
                                cb.eq(rt.get("id"), det.get("id"))
                        ));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id from EMPLOYEES tbl full join EMPLOYEE_DETAILS det on tbl.ID = det.ID", select);
    }

    @Test
    public void should__inner_join() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Root<EmployeeDetails> det = cr.from(EmployeeDetails.class).as("det");

        cr
                .from(
                        rt.innerJoin(det)
                                .on(
                                        cb.eq(rt.get("id"), det.get("id"))
                                )
                )
                .select(rt.get("id"));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id from EMPLOYEES tbl inner join EMPLOYEE_DETAILS det on tbl.ID = det.ID", select);
    }

    @Test
    public void should__cross_join() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Root<EmployeeDetails> det = cr.from(EmployeeDetails.class).as("det");

        cr
                .select(rt.get("id"))
                .from(rt.join(det));

        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id from EMPLOYEES tbl cross join EMPLOYEE_DETAILS det", select);
    }

    @Test
    public void should__complex_join() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Root<EmployeeDetails> det = cr.from(EmployeeDetails.class).as("det");

        cr.select(rt.get("id"))
                .from(
                        rt.leftJoin(det).on(cb.eq(rt.get("id"), det.get("id")))
                                .rightJoin(det).on(cb.eq(rt.get("id"), det.get("id"))
                                ));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id from EMPLOYEES tbl left join EMPLOYEE_DETAILS det on tbl.ID = det.ID right join EMPLOYEE_DETAILS det on tbl.ID = det.ID", select);
    }

    @Test
    public void should__not_operation() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Expression not = cb.not(
                cb.eq(
                                rt.get("id"), 3
                        )
                        .and(
                                cb.not(
                                        cb.eq(rt.get("id"), 4)
                                ))

        );
        CriteriaQuery<?> criteria = cr
                .select(rt)
                .where(not);
        String select = criteria.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl where ( NOT (tbl.ID = 3 and ( NOT (tbl.ID = 4))))", select);
    }

    @Test
    public void should__in_operation() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr
                .select(rt)
                .where(
                        cb.in(
                                rt.get("id"), 3, 4, 5
                        )

                );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl where (tbl.ID in (3,4,5))", select);
    }

    @Test
    public void should__inn_operation() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr
                .select(rt)
                .where(
                        cb.inn(
                                rt.get("id"), List.of()
                        )

                );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl", select);
    }

    @Test
    public void should__delete_query() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Expression and = cb.and(
                cb.gt(rt.get("id"), 0),
                cb.lt(rt.get("id"), 30),
                cb.gt(rt.get("id"), 10),
                cb.lt(rt.get("id"), 20),
                cb.gt(rt.get("id"), 17),
                cb.lt(rt.get("id"), 19)
        );
        cr
                .select(rt)
                .where(and);
        String select = cr.buildDeleteQuery();
        Assertions.assertEquals("delete from EMPLOYEES tbl where ((tbl.ID > 0 and (tbl.ID < 30 and tbl.ID > 10 and tbl.ID < 20 and tbl.ID > 17 and tbl.ID < 19)))", select);
    }

    @Test
    public void should__select_ends_with_str() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(rt.get("firstName"))
                .where(cb.endsWith(rt.get("firstName"), "ABC"));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.FIRST_NAME as firstName from EMPLOYEES tbl where (tbl.FIRST_NAME like '%ABC')", select);
    }

    @Test
    public void should__select_starts_with_str() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(rt.get("firstName"))
                .where(cb.startsWith(rt.get("firstName"), "ABC"));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.FIRST_NAME as firstName from EMPLOYEES tbl where (tbl.FIRST_NAME like 'ABC%')", select);
    }

    @Test
    public void should__select_count_path() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.count(rt.get("firstName")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select count(tbl.FIRST_NAME) from EMPLOYEES tbl", select);
    }

    @Test
    public void should__select_count_all() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.count(rt));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select count(*) from EMPLOYEES tbl", select);
    }

    @Test
    public void should__select_distinct() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.distinct(rt.get("id")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select distinct tbl.ID from EMPLOYEES tbl", select);
    }

    @Test
    public void should__select_from_schema() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class)
                .schema("DB")
                .as("tbl");

        cr.select(rt.get("id"));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id from DB.EMPLOYEES tbl", select);
    }

    @Test
    public void should__select_count_distinct() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.count(cb.distinct(rt.get("id"))));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select count(distinct tbl.ID) from EMPLOYEES tbl", select);
    }

    @Test
    public void should__group_by_with_count() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Path<Employee> id = rt.get("id");
        cr.select(id, cb.count(rt.get("firstName")))
                .where(cb.gt(id, 3))
                .groupBy(id)
                .orderBy(id);
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select count(tbl.FIRST_NAME),tbl.ID as id from EMPLOYEES tbl where (tbl.ID > 3) group by tbl.ID order by tbl.ID ASC", select);
    }

    @Test
    void should__having() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");

        String sql = query
                .select(emp)
                .having(cb.gt(cb.avg(emp.get("salary")), 30)).buildSelectQuery();
        assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl having (avg(tbl.SALARY) > 30)", sql);
    }

    @Test
    void should__group__by__having() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> root = cr.from(Employee.class).as("tbl");

        String sql = cr
                .select(root.get("id"))
                .groupBy(root.get("salary"))
                .having(cb.gt(cb.avg(root.get("salary")), 30))
                .buildSelectQuery();

        assertEquals("select tbl.ID as id from EMPLOYEES tbl group by tbl.SALARY having (avg(tbl.SALARY) > 30)", sql);
    }

    @Test
    public void should_generate_insert_one_field() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        var rt = cr.from(Employee.class);
        cr.select(rt.get("id"));
        String insert = cr.buildInsertQuery();
        Assertions.assertEquals("insert into EMPLOYEES (ID) values (:id)", insert);
    }

    @Test
    public void should_generate_insert_multiple_fields() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        var rt = cr.from(Employee.class);
        cr.select(rt.get("id"), rt.get("firstName"));
        String insert = cr.buildInsertQuery();
        Assertions.assertEquals("insert into EMPLOYEES (ID,FIRST_NAME) values (:id,:firstName)", insert);
    }

    @Test
    public void should_generate_insert_all_fields() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        String insert = cr
                .select(cr.from(Employee.class))
                .buildInsertQuery();
        Assertions.assertEquals("insert into EMPLOYEES (ENABLED,FIRST_NAME,ID,LAST_NAME,SALARY) values (:enabled,:firstName,:id,:lastName,:salary)", insert);
    }

    // @Test()
    public void should_insert_with_table_name_mapper() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        //cr.from(s -> "tab_" + s);
        String insert = cr.buildInsertQuery();
        Assertions.assertEquals("insert into tab_EMPLOYEES (ENABLED,FIRST_NAME,ID,LAST_NAME,SALARY) values (:enabled,:firstName,:id,:lastName,:salary)", insert);
    }

    //@Test
    public void should_insert_with_prefix_2() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        //cr.prefix("tab_");
        String insert = cr.buildInsertQuery();
        Assertions.assertEquals("insert into tab_EMPLOYEES (ENABLED,FIRST_NAME,ID,LAST_NAME,SALARY) values (:enabled,:firstName,:id,:lastName,:salary)", insert);
    }

    @Test
    public void exists_subquery() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class).as("emp");
        //
        CriteriaQuery<EmployeeDetails> sub = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> subRoot = sub.from(EmployeeDetails.class).as("pro");
        //
        String sql = query
                .select(root)
                .where(
                        cb.exists(sub
                                .select(subRoot)
                                .where(cb.eq(root.get("id"), subRoot.get("id")))
                        )
                ).buildSelectQuery();
        assertEquals("select emp.ENABLED as enabled,emp.FIRST_NAME as firstName,emp.ID as id,emp.LAST_NAME as lastName,emp.SALARY as salary from EMPLOYEES emp where ( exists (select pro.DEPARTMENT_ID as departmentId,pro.EMPLOYEE_ID as employeeId,pro.ID as id from EMPLOYEE_DETAILS pro where (emp.ID = pro.ID)))", sql);
    }

    @Test
    public void any_subquery() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);

        Root<Employee> root = query.from(Employee.class).as("tbl");
        CriteriaQuery<Employee> emp = cb.createQuery(Employee.class);
        Root<Employee> empRoot = query.from(Employee.class);
        //
        String sql = query
                .select(root)
                .where(
                        cb.gt(
                                root.get("id"), cb.any(emp.select(empRoot.get("id")).from(empRoot).where(cb.eq(empRoot.get("id"), 1L)))
                        )
                ).buildSelectQuery();
        assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl where (tbl.ID > any (select EMPLOYEES.ID as id from EMPLOYEES EMPLOYEES where (EMPLOYEES.ID = 1)))", sql);
    }

    @Test
    public void all_subquery() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        CriteriaQuery<Employee> sub = cb.createQuery(Employee.class);
        Root<Employee> r = sub.from(Employee.class).as("sub");
        String sql = query
                .select(root)
                .where(
                        cb.ge(root.get("id"), cb.all(sub.select(r.get("id"))))
                ).buildSelectQuery();
        assertEquals("select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.ID >= all (select sub.ID as id from EMPLOYEES sub))", sql);
    }

    @Test
    public void scalar_select() {
        CriteriaQuery<Department> dep = cb.createQuery(Department.class);
        Root<Department> root = dep.from(Department.class);
        CriteriaQuery<Employee> emp = cb.createQuery(Employee.class);
        Root<Employee> empRoot = emp.from(Employee.class);
        String sql = dep.select(root.get("name"), cb.scalar(emp.select(cb.max(empRoot.get("salary"))))).buildSelectQuery();
        assertEquals("select (select max(EMPLOYEES.SALARY) from EMPLOYEES EMPLOYEES),DEPARTMENTS.NAME as name from DEPARTMENTS DEPARTMENTS", sql);
    }

    @Test
    void testLimitOffset() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class);

        query.select(emp).limit(5).offset(10);
        String sql = query.buildSelectQuery();
        assertEquals("select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES limit 5 offset 10", sql);

        query.limit(null).offset(null); // Test with null values
        sql = query.buildSelectQuery();
        assertEquals("select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES", sql); //no limit or offset


        query.limit(50).offset(null); //Just limit
        sql = query.buildSelectQuery();
        assertEquals("select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES limit 50", sql);


        query.limit(null).offset(150); //Just offset
        sql = query.buildSelectQuery();
        assertEquals("select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES offset 150", sql);
    }


    @Test
    void testUnion() {
        CriteriaQuery<Employee> query1 = cb.createQuery(Employee.class);
        Root<Employee> emp1 = query1.from(Employee.class);
        query1
                .select(emp1)
                .where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2
                .select(emp2)
                .where(cb.eq(emp2.get("departmentId"), 2));

        String unionSql = query1.union(query2).buildSelectQuery();
        String expectedSql = "select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 1) union (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 2))";
        assertEquals(expectedSql, unionSql);
    }

    @Test
    void testUnionAll() {
        CriteriaQuery<Employee> query1 = cb.createQuery(Employee.class);
        Root<Employee> emp1 = query1.from(Employee.class);
        query1
                .select(emp1)
                .where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2
                .select(emp2)
                .where(cb.eq(emp2.get("departmentId"), 2));

        String unionSql = query1.unionAll(query2).buildSelectQuery();
        String expectedSql = "select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 1) union all (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 2))";
        assertEquals(expectedSql, unionSql);
    }

    @Test
    void testIntersect() {
        CriteriaQuery<Employee> query1 = cb.createQuery(Employee.class);
        Root<Employee> emp1 = query1.from(Employee.class);
        query1
                .select(emp1)
                .where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2
                .select(emp2)
                .where(cb.eq(emp2.get("departmentId"), 2));

        String intersectSql = query1.intersect(query2).buildSelectQuery();
        String expectedSql = "select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 1) intersect (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 2))";

        assertEquals(expectedSql, intersectSql);
    }


    @Test
    void testExcept() {
        CriteriaQuery<Employee> query1 = cb.createQuery(Employee.class);
        Root<Employee> emp1 = query1.from(Employee.class);
        query1
                .select(emp1)
                .where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2
                .select(emp2)
                .where(cb.eq(emp2.get("departmentId"), 2));

        String exceptSql = query1.except(query2).buildSelectQuery();

        String expectedSql = "select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 1) except (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 2))";
        assertEquals(expectedSql, exceptSql);
    }

    @Test
    void testDerivedTable() {

        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> depRoot = query.from(Department.class).as("d");
        //
        CriteriaQuery<?> query1 = cb.createQuery(Employee.class); //
        Root<Employee> emp = query1.from(Employee.class).as("e");
        query1.select(emp)
                .where(cb.gt(emp.get("salary"), 1000));

        Subquery subquery = new Subquery(query1);
        subquery.setAlias("e");//alias overload
        query.select(depRoot.get("name"), emp.get("firstName"))
                .from(depRoot.rightJoin(subquery).on(cb.eq(depRoot.get("id"), emp.get("id"))));
        String sql = query.buildSelectQuery(); // Build the query from the outer query
        assertEquals("select e.FIRST_NAME as firstName,d.NAME as name from DEPARTMENTS d right join (select e.ENABLED as enabled,e.FIRST_NAME as firstName,e.ID as id,e.LAST_NAME as lastName,e.SALARY as salary from EMPLOYEES e where (e.SALARY > 1000)) e on d.ID = e.ID", sql);
    }

    @Test
    void testUnionAndIntersect() {
        CriteriaQuery<Employee> query1 = cb.createQuery(Employee.class);
        Root<Employee> emp1 = query1.from(Employee.class);
        query1
                .select(emp1)
                .where(cb.between(emp1.get("salary"), 0, 1300));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2
                .select(emp2)
                .where(cb.between(emp2.get("salary"), 1000, 6000));

        CriteriaQuery<Employee> query3 = cb.createQuery(Employee.class);
        Root<Employee> emp3 = query3.from(Employee.class);
        query3
                .select(emp3)
                .where(cb.gt(emp3.get("salary"), 1000));

        String combinedSql = query1
                .union(query2)
                .intersect(query3)
                .buildSelectQuery();
        String expectedSql = "select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.SALARY between 0 and 1300) union (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.SALARY between 1000 and 6000)) intersect (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.SALARY > 1000))";
        assertEquals(expectedSql, combinedSql);
    }

    @Test
    void testUnionAllAndExcept() {
        CriteriaQuery<EmployeeDetails> query1 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp1 = query1.from(EmployeeDetails.class).as("det");
        query1
                .select(emp1)
                .where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<EmployeeDetails> query2 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp2 = query2.from(EmployeeDetails.class).as("det");
        query2
                .select(emp2)
                .where(cb.eq(emp2.get("departmentId"), 2));

        CriteriaQuery<EmployeeDetails> query3 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp3 = query3.from(EmployeeDetails.class).as("det");
        query3
                .select(emp3)
                .where(cb.eq(emp3.get("departmentId"), 3));


        String combinedSql = query1.unionAll(query2).except(query3).buildSelectQuery();
        String expectedSql = "select det.DEPARTMENT_ID as departmentId,det.EMPLOYEE_ID as employeeId,det.ID as id from EMPLOYEE_DETAILS det where (det.DEPARTMENT_ID = 1) union all (select det.DEPARTMENT_ID as departmentId,det.EMPLOYEE_ID as employeeId,det.ID as id from EMPLOYEE_DETAILS det where (det.DEPARTMENT_ID = 2)) except (select det.DEPARTMENT_ID as departmentId,det.EMPLOYEE_ID as employeeId,det.ID as id from EMPLOYEE_DETAILS det where (det.DEPARTMENT_ID = 3))";
        assertEquals(expectedSql, combinedSql);
    }

    @Test
    void testIntersectAndUnion() {
        CriteriaQuery<EmployeeDetails> query1 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp1 = query1.from(EmployeeDetails.class).as("det");
        query1
                .select(emp1)
                .where(cb.eq(emp1.get("departmentId"), 1));


        CriteriaQuery<EmployeeDetails> query2 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp2 = query2.from(EmployeeDetails.class).as("det");
        query2
                .select(emp2)
                .where(cb.eq(emp2.get("departmentId"), 2));

        CriteriaQuery<EmployeeDetails> query3 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp3 = query3.from(EmployeeDetails.class).as("det");
        query3
                .select(emp3)
                .where(cb.eq(emp3.get("departmentId"), 3));

        String combinedSql = query1.intersect(query2).union(query3).buildSelectQuery();
        String expectedSql = "select det.DEPARTMENT_ID as departmentId,det.EMPLOYEE_ID as employeeId,det.ID as id from EMPLOYEE_DETAILS det where (det.DEPARTMENT_ID = 1) intersect (select det.DEPARTMENT_ID as departmentId,det.EMPLOYEE_ID as employeeId,det.ID as id from EMPLOYEE_DETAILS det where (det.DEPARTMENT_ID = 2)) union (select det.DEPARTMENT_ID as departmentId,det.EMPLOYEE_ID as employeeId,det.ID as id from EMPLOYEE_DETAILS det where (det.DEPARTMENT_ID = 3))";
        assertEquals(expectedSql, combinedSql);
    }

    @Test
    void testEmbeddedUnion() {
        CriteriaQuery<EmployeeDetails> query1 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp1 = query1.from(EmployeeDetails.class).as("det");
        query1
                .select(emp1)
                .where(cb.eq(emp1.get("departmentId"), 1));


        CriteriaQuery<EmployeeDetails> query2 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp2 = query2.from(EmployeeDetails.class).as("det");
        query2
                .select(emp2)
                .where(cb.eq(emp2.get("departmentId"), 2));

        CriteriaQuery<EmployeeDetails> query3 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp3 = query3.from(EmployeeDetails.class).as("det");
        query3
                .select(emp3)
                .where(cb.eq(emp3.get("departmentId"), 3));

        String combinedSql = query1.intersect(query2.union(query3)).buildSelectQuery();
        String expectedSql = "select det.DEPARTMENT_ID as departmentId,det.EMPLOYEE_ID as employeeId,det.ID as id from EMPLOYEE_DETAILS det where (det.DEPARTMENT_ID = 1) intersect (select det.DEPARTMENT_ID as departmentId,det.EMPLOYEE_ID as employeeId,det.ID as id from EMPLOYEE_DETAILS det where (det.DEPARTMENT_ID = 2) union (select det.DEPARTMENT_ID as departmentId,det.EMPLOYEE_ID as employeeId,det.ID as id from EMPLOYEE_DETAILS det where (det.DEPARTMENT_ID = 3)))";
        assertEquals(expectedSql, combinedSql);
    }

    @Test
    void testUnionWithOrderByAndLimit() {
        CriteriaQuery<EmployeeDetails> query1 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp1 = query1.from(EmployeeDetails.class);
        query1
                .select(emp1)
                .where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<EmployeeDetails> query2 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp2 = query2.from(EmployeeDetails.class);
        query2
                .select(emp2)
                .where(cb.eq(emp2.get("departmentId"), 2));

        CriteriaQuery<EmployeeDetails> combinedQuery = query1.union(query2);
        combinedQuery
                .select()
                .orderBy(emp1.get("employeeId"), OrderDirection.ASC); // Order by first name
        combinedQuery.limit(10); // Limit to 10 results


        String sql = combinedQuery
                .buildSelectQuery();
        // ORDER BY and LIMIT should apply to the entire UNION
        String expectedSql = "select * from (select EMPLOYEE_DETAILS.DEPARTMENT_ID as departmentId,EMPLOYEE_DETAILS.EMPLOYEE_ID as employeeId,EMPLOYEE_DETAILS.ID as id from EMPLOYEE_DETAILS EMPLOYEE_DETAILS where (EMPLOYEE_DETAILS.DEPARTMENT_ID = 1) union (select EMPLOYEE_DETAILS.DEPARTMENT_ID as departmentId,EMPLOYEE_DETAILS.EMPLOYEE_ID as employeeId,EMPLOYEE_DETAILS.ID as id from EMPLOYEE_DETAILS EMPLOYEE_DETAILS where (EMPLOYEE_DETAILS.DEPARTMENT_ID = 2))) nested_query order by nested_query.employeeId ASC limit 10";
        assertEquals(expectedSql, sql);
    }


    @Test
    void testIntersectWithOrderByAndLimit() {

        CriteriaQuery<Employee> query1 = cb.createQuery(Employee.class);
        Root<Employee> emp1 = query1.from(Employee.class);
        query1
                .select(emp1)
                .where(cb.gt(emp1.get("salary"), 1000));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2
                .select(emp2)
                .where(cb.lt(emp2.get("salary"), 2000));

        CriteriaQuery<Employee> combinedQuery = query1.intersect(query2);
        combinedQuery.orderBy(emp1.get("firstName"), OrderDirection.ASC);
        combinedQuery.limit(5).offset(0);

        String sql = combinedQuery.buildSelectQuery();
        String expectedSql = "select * from (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.SALARY > 1000) intersect (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.SALARY < 2000))) nested_query order by nested_query.firstName ASC limit 5 offset 0";
        assertEquals(expectedSql, sql);
    }


    @Test
    void testExceptWithOrderByAndLimit() {

        CriteriaQuery<EmployeeDetails> query1 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp1 = query1.from(EmployeeDetails.class);
        query1
                .select(emp1)
                .where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<EmployeeDetails> query2 = cb.createQuery(EmployeeDetails.class);
        Root<EmployeeDetails> emp2 = query2.from(EmployeeDetails.class);
        query2
                .select(emp2)
                .where(cb.eq(emp2.get("departmentId"), 2));


        CriteriaQuery<EmployeeDetails> combinedQuery = query1.except(query2);
        combinedQuery.orderBy(emp1.get("employeeId"), OrderDirection.ASC);
        combinedQuery.limit(50);

        String sql = combinedQuery.buildSelectQuery();
        String expectedSql = "select * from (select EMPLOYEE_DETAILS.DEPARTMENT_ID as departmentId,EMPLOYEE_DETAILS.EMPLOYEE_ID as employeeId,EMPLOYEE_DETAILS.ID as id from EMPLOYEE_DETAILS EMPLOYEE_DETAILS where (EMPLOYEE_DETAILS.DEPARTMENT_ID = 1) except (select EMPLOYEE_DETAILS.DEPARTMENT_ID as departmentId,EMPLOYEE_DETAILS.EMPLOYEE_ID as employeeId,EMPLOYEE_DETAILS.ID as id from EMPLOYEE_DETAILS EMPLOYEE_DETAILS where (EMPLOYEE_DETAILS.DEPARTMENT_ID = 2))) nested_query order by nested_query.employeeId ASC limit 50";

        assertEquals(expectedSql, sql);
    }

    @Test
    void testCorrelatedSubquery() {
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> dep = query.from(Department.class);

        CriteriaQuery<Employee> subquery = cb.createQuery(Employee.class);  // Assuming you have a subquery method
        Root<Employee> emp = subquery.from(Employee.class);
        subquery
                .select(emp.get("salary")).where(cb.eq(emp.get("departmentId"), dep.get("id"))); // Correlating condition

        query.select(dep).where(cb.gt(dep.get("budget"), new Subquery(subquery))); // Using the correlated subquery

        String expectedSql = "select DEPARTMENTS.ID as id,DEPARTMENTS.LOCATION as location,DEPARTMENTS.NAME as name from DEPARTMENTS DEPARTMENTS where (DEPARTMENTS.budget > (select EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = DEPARTMENTS.ID)) )";  // Verify correct correlation
        assertEquals(expectedSql, query.buildSelectQuery());
    }

    @Test
    void simple_case() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");
        String sql = query
                .select(
                        emp.get("firstName"),
                        cb.choice(emp.get("salary"))
                                .when(100)
                                .then("slave")
                                .when(200)
                                .then("employee")
                                .otherwise("rich")
                                .as("status")
                ).from(emp)
                .buildSelectQuery();

        assertEquals("select tbl.FIRST_NAME as firstName,case tbl.SALARY when 100 then 'slave' when 200 then 'employee' else 'rich' end as status from EMPLOYEES tbl", sql);
    }

    @Test
    void searched_case() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");
        String sql = query
                .select(
                        emp.get("firstName"),
                        cb.choice()
                                .whenThen(cb.eq(emp.get("salary"), 100), "slave")
                                .when(cb.eq(emp.get("salary"), 200))
                                .then("employee")
                                .otherwise("rich")
                                .as("status")
                ).from(emp)
                .buildSelectQuery();
        assertEquals("select tbl.FIRST_NAME as firstName,case when tbl.SALARY = 100 then 'slave' when tbl.SALARY = 200 then 'employee' else 'rich' end as status from EMPLOYEES tbl", sql);
    }

    @Test
    void nested_case() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class);
        Selectable outerCase = cb.choice()
                .when(cb.gt(emp.get("age"), 60))
                .then("Retired")
                .otherwise(
                        cb.choice(emp.get("departmentId"))  // Inner Simple CASE
                                .when(1)
                                .then("Sales")
                                .whenThen(2, "Marketing")
                                .otherwise("Other")

                ).end().as("status");
        String sql = query.select(outerCase).buildSelectQuery();
        assertEquals("select case when EMPLOYEES.age > 60 then 'Retired' else case EMPLOYEES.departmentId when 1 then 'Sales' when 2 then 'Marketing' else 'Other' end end as status from EMPLOYEES EMPLOYEES", sql);
    }

    @Test
    void simple_cte() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> root = cr.from(Employee.class);
        Cte<?> cte = cb.with("my_query").as(
                cr.select()
                        .from(root)
        );
        String sql = cte
                .select(cte.get("firstName"))
                .from(cte)
                .buildSelectQuery();
        assertEquals("with my_query as (select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES) select my_query.firstName as firstName from my_query my_query", sql);
    }

    /**
     * with my_query as (select sum(EMPLOYEES.salary) as salary, EMPLOYEES.last_name as lastName
     * from EMPLOYEES EMPLOYEES
     * group by last_name)
     * select my_query.salary as salary, my_query.lastName as lastName
     * from my_query my_query
     */

    @Test
    void complex_cte() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> root = cr.from(Employee.class);
        Cte<?> cte = cb.with("my_query").as(
                cr.select(cb.sum(root.get("salary")).as("salary"), root.get("lastName"))
                        .from(root)
                        .groupBy(root.get("lastName"))
        );
        String sql = cte
                .select(cte.get("lastName"), cte.get("salary"))
                .from(cte)
                .buildSelectQuery();
        assertEquals("with my_query as (select EMPLOYEES.LAST_NAME as lastName,sum(EMPLOYEES.SALARY) as salary from EMPLOYEES EMPLOYEES group by EMPLOYEES.LAST_NAME) select my_query.lastName as lastName,my_query.salary as salary from my_query my_query", sql);
    }


    @Test
    void testJoinWithComplexSubqueryCondition() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("e");
        Root<Department> dept = query.from(Department.class).as("d");

        CriteriaQuery<Employee> subquery1 = cb.createQuery(Employee.class);
        Root<Employee> sqEmp1 = subquery1.from(Employee.class).as("sqe1");
        subquery1.select(sqEmp1.get("departmentId"))
                .where(cb.gt(sqEmp1.get("salary"), 50000));

        CriteriaQuery<Department> subquery2 = cb.createQuery(Department.class);
        Root<Department> sqDept = subquery2.from(Department.class).as("sqd");
        subquery2.select(sqDept.get("id"))
                .where(cb.like(sqDept.get("name"), "%Engineering%"));


        query.select(emp)
                .from(emp.innerJoin(dept)
                        .on(
                                cb.and(
                                        cb.in(dept.get("id"), new Subquery(subquery1)),  // Subquery in IN clause
                                        cb.not(cb.in(dept.get("id"), new Subquery(subquery2))) // Subquery in NOT IN clause, combined with NOT
                                )
                        ));


        String expectedSql = "select e.ENABLED as enabled,e.FIRST_NAME as firstName,e.ID as id,e.LAST_NAME as lastName,e.SALARY as salary from EMPLOYEES e inner join DEPARTMENTS d on (d.ID in ( (select sqe1.departmentId as departmentId from EMPLOYEES sqe1 where (sqe1.SALARY > 50000)) ) and ( NOT (d.ID in ( (select sqd.ID as id from DEPARTMENTS sqd where (sqd.NAME like '%%Engineering%%')) ))))";
        assertEquals(expectedSql, query.buildSelectQuery());

    }

    @Test
    void testCaseWithNullHandling() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");

        // Test NULL handling in WHEN clauses
        Selectable caseExpression = cb.choice(emp.get("departmentId"))
                .when(null).then("Unknown Department")       // WHEN departmentId IS NULL
                .when(1).then("Sales")
                .otherwise("Other").as("departmentName");

        String sql = query.select(caseExpression).buildSelectQuery();
        assertEquals("select case tbl.departmentId when null then 'Unknown Department' when 1 then 'Sales' else 'Other' end as departmentName from EMPLOYEES tbl", sql);


        // Test NULL handling with IS NULL predicate in WHEN clause (Searched CASE)
        caseExpression = cb.choice()
                .when(cb.isNull(emp.get("departmentId"))).then("No Department Assigned")
                .when(cb.eq(emp.get("departmentId"), 1)).then("Sales")
                .otherwise("Other");
        sql = query.select(caseExpression).buildSelectQuery();

        assertEquals("select case when tbl.departmentId is null then 'No Department Assigned' when tbl.departmentId = 1 then 'Sales' else 'Other' end from EMPLOYEES tbl", sql);

    }

    @Test
    void should_generate_named_update_query() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");
        String sql = query
                .select(emp)
                .from(emp)
                .where(cb.eq(emp.get("id"), 1))
                .buildNamedUpdateQuery();
        assertEquals("update EMPLOYEES tbl set ENABLED = :enabled, FIRST_NAME = :firstName, ID = :id, LAST_NAME = :lastName, SALARY = :salary", sql);
    }

}