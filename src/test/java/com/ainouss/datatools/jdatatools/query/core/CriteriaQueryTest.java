package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.model.Department;
import com.ainouss.datatools.jdatatools.query.model.Employee;
import com.ainouss.datatools.jdatatools.query.model.Profile;
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
        cr.where(
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
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl where (tbl.FIRST_NAME = 'XYZ' and (tbl.SALARY like '%TEST%' and tbl.LAST_NAME is null and tbl.ENABLED is not null ) or (tbl.FIRST_NAME = 'ABCD' and (tbl.ID = 1000) or tbl.FIRST_NAME = 'ABBFSTO' and (tbl.ID = 15 or (tbl.ID between 5 and 1000))))", select);
    }

    @Test
    public void should__with_complex_where() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.where(
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
        Root<Profile> child = cr.from(Profile.class).as("child");
        Expression and = cb.and(
                cb.eq(child.get("id"), 0),
                cb.gt(rt.get("id"), 1)
        );
        cr.where(and)
                .join(
                        rt.leftJoin(child)
                                .on(
                                        cb.eq(rt.get("id"), child.get("id"))
                                )
                );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl left join PROFILES child on tbl.ID = child.ID where ((child.ID = 0 and (tbl.ID > 1)))", select);
    }

    @Test
    public void should__left_join_with_schema() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class)
                .schema("DB")
                .as("tbl");
        Root<Profile> child = cr.from(Profile.class)
                .schema("DB2")
                .as("child");

        Expression and = cb.and(
                cb.eq(child.get("id"), 0),
                cb.gt(rt.get("id"), 1)
        );
        cr.where(and)
                .join(
                        rt.leftJoin(child)
                                .on(
                                        cb.eq(rt.get("id"), child.get("id"))
                                )
                );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from DB.EMPLOYEES tbl left join DB2.PROFILES child on tbl.ID = child.ID where ((child.ID = 0 and (tbl.ID > 1)))", select);
    }

    @Test
    public void should__select_max() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.max(rt.get("id")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select max(tbl.ID) as id from EMPLOYEES tbl", select);
    }

    @Test
    public void should__select_min() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.min(rt.get("id")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select min(tbl.ID) as id from EMPLOYEES tbl", select);
    }


    @Test
    public void should__select_sum() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.sum(rt.get("id")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select sum(tbl.ID) as id from EMPLOYEES tbl", select);
    }

    @Test
    public void should__select_avg() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.select(cb.avg(rt.get("id")));
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select avg(tbl.ID) as id from EMPLOYEES tbl", select);
    }

    @Test
    public void should__full_join() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Root<Profile> child = cr.from(Profile.class).as("child");

        cr.select(rt.get("id"))
                .join(
                        rt.fullJoin(child)
                                .on(
                                        cb.eq(rt.get("id"), child.get("id"))
                                )
                );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id from EMPLOYEES tbl full join PROFILES child on tbl.ID = child.ID", select);
    }

    @Test
    public void should__inner_join() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Root<Profile> child = cr.from(Profile.class).as("child");

        cr.select(rt.get("id"))
                .join(
                        rt.innerJoin(child)
                                .on(
                                        cb.eq(rt.get("id"), child.get("id"))
                                )
                );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id from EMPLOYEES tbl inner join PROFILES child on tbl.ID = child.ID", select);
    }

    @Test
    public void should__cross_join() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Root<Profile> child = cr.from(Profile.class).as("child");

        cr.select(rt.get("id"))
                .join(
                        rt.join(child)
                );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id from EMPLOYEES tbl cross join PROFILES child", select);
    }

    @Test
    public void should__complex_join() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Root<Profile> child = cr.from(Profile.class).as("child");

        cr.select(rt.get("id"))
                .join(
                        rt.leftJoin(child)
                                .on(
                                        cb.eq(rt.get("id"), child.get("id"))
                                )
                ).join(
                        rt.rightJoin(child)
                                .on(
                                        cb.eq(rt.get("id"), child.get("id"))
                                )
                );
        String select = cr.buildSelectQuery();
        Assertions.assertEquals("select tbl.ID as id from EMPLOYEES tbl left join PROFILES child on tbl.ID = child.ID right join PROFILES child on tbl.ID = child.ID", select);
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
        CriteriaQuery<?> criteria = cr.where(not);
        String select = criteria.buildSelectQuery();
        Assertions.assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl where ( NOT (tbl.ID = 3 and ( NOT (tbl.ID = 4))))", select);
    }

    @Test
    public void should__in_operation() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        cr.where(
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
        cr.where(
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
        cr.where(and);
        String select = cr.buildDeleteQuery();
        Assertions.assertEquals("delete from EMPLOYEES tbl where ((tbl.ID > 0 and (tbl.ID < 30 and tbl.ID > 10 and tbl.ID < 20 and tbl.ID > 17 and tbl.ID < 19)))", select);
    }

    @Test
    public void should__when_then_otherwise() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        Root<Employee> rt = cr.from(Employee.class).as("tbl");
        Expression and = cb.when(true)
                .then(
                        cb.eq(rt.get("id"), 1)
                ).otherwise(
                        cb.eq(rt.get("id"), 2)
                )
                .end()
                .and(cb.eq(rt.get("id"), 3));
        cr.where(and);
        String select = cr.buildDeleteQuery();
        Assertions.assertEquals("delete from EMPLOYEES tbl where (tbl.ID = 1 and (tbl.ID = 3))", select);
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
        Assertions.assertEquals("select count(tbl.FIRST_NAME) as firstName from EMPLOYEES tbl", select);
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
        Assertions.assertEquals("select distinct tbl.ID as id from EMPLOYEES tbl", select);
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
        Assertions.assertEquals("select count(distinct tbl.ID) as id from EMPLOYEES tbl", select);
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
        Assertions.assertEquals("select count(tbl.FIRST_NAME) as firstName,tbl.ID as id from EMPLOYEES tbl where (tbl.ID > 3) group by tbl.ID order by tbl.ID ASC", select);
    }

    @Test
    void should__having() {
        CriteriaBuilder cb = new CriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> emp = query.from(Employee.class).as("tbl");
        String sql = query.having(cb.gt(cb.avg(emp.get("salary")), 30)).buildSelectQuery();
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
        Assertions.assertEquals("insert into EMPLOYEES (ENABLED,FIRST_NAME,ID,LAST_NAME,SALARY) values (:enabled,:firstName,:id,:lastName,:salary)", insert);
    }

    @Test
    public void should_insert_with_table_name_mapper() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        cr.from(s -> "tab_" + s);
        String insert = cr.buildInsertQuery();
        Assertions.assertEquals("insert into tab_EMPLOYEES (ENABLED,FIRST_NAME,ID,LAST_NAME,SALARY) values (:enabled,:firstName,:id,:lastName,:salary)", insert);
    }

    @Test
    public void should_insert_with_prefix_2() {
        CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
        cr.prefix("tab_");
        String insert = cr.buildInsertQuery();
        Assertions.assertEquals("insert into tab_EMPLOYEES (ENABLED,FIRST_NAME,ID,LAST_NAME,SALARY) values (:enabled,:firstName,:id,:lastName,:salary)", insert);
    }

    @Test
    public void exists_subquery() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class).as("emp");
        Root<Employee> root = query.from();
        CriteriaQuery<Profile> sub = cb.createQuery(Profile.class).as("pro");
        Root<Profile> subRoot = sub.from();
        String sql = query.where(
                cb.exists(sub.where(cb.eq(root.get("id"), subRoot.get("id"))))
        ).buildSelectQuery();
        assertEquals("select emp.ENABLED as enabled,emp.FIRST_NAME as firstName,emp.ID as id,emp.LAST_NAME as lastName,emp.SALARY as salary from EMPLOYEES emp where ( exists (select pro.ID as id,pro.VALUE as value from PROFILES pro where (emp.ID = pro.ID)))", sql);
    }

    @Test
    public void any_subquery() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class)
                .as("tbl");
        Root<Employee> root = query.from();
        CriteriaQuery<Employee> emp = cb.createQuery(Employee.class);
        Root<Employee> empRoot = emp.from();
        //
        String sql = query.where(
                cb.gt(
                        root.get("id"), cb.any(emp.select(empRoot.get("id")).where(cb.eq(empRoot.get("id"), 1L)))
                )
        ).buildSelectQuery();
        assertEquals("select tbl.ENABLED as enabled,tbl.FIRST_NAME as firstName,tbl.ID as id,tbl.LAST_NAME as lastName,tbl.SALARY as salary from EMPLOYEES tbl where (tbl.ID > any (select EMPLOYEES.ID as id from EMPLOYEES EMPLOYEES where (EMPLOYEES.ID = 1)))", sql);
    }

    @Test
    public void all_subquery() {
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from();
        CriteriaQuery<Employee> sub = cb.createQuery(Employee.class).as("sub");
        Root<Employee> r = sub.from();
        String sql = query.where(
                cb.ge(root.get("id"), cb.all(sub.select(r.get("id"))))
        ).buildSelectQuery();
        assertEquals("select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.ID >= all (select sub.ID as id from EMPLOYEES sub))", sql);
    }

    @Test
    public void scalar_select() {
        CriteriaQuery<Department> dep = cb.createQuery(Department.class);
        Root<Department> root = dep.from();
        CriteriaQuery<Employee> emp = cb.createQuery(Employee.class);
        Root<Employee> empRoot = emp.from();
        String sql = dep.select(root.get("name"), cb.scalar(emp.select(cb.max(empRoot.get("salary"))))).buildSelectQuery();
        assertEquals("select DEPARTMENTS.NAME as name, (select max(EMPLOYEES.SALARY) as salary from EMPLOYEES EMPLOYEES) as salary from DEPARTMENTS DEPARTMENTS", sql);
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
        query1.where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2.where(cb.eq(emp2.get("departmentId"), 2));

        String unionSql = query1.union(query2).buildSelectQuery();
        String expectedSql = "select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 1) union select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 2)";
        assertEquals(expectedSql, unionSql);
    }

    @Test
    void testUnionAll() {
        CriteriaQuery<Employee> query1 = cb.createQuery(Employee.class);
        Root<Employee> emp1 = query1.from(Employee.class);
        query1.where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2.where(cb.eq(emp2.get("departmentId"), 2));

        String unionSql = query1.unionAll(query2).buildSelectQuery();
        String expectedSql = "select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 1) union all select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 2)";
        assertEquals(expectedSql, unionSql);
    }

    @Test
    void testIntersect() {
        CriteriaQuery<Employee> query1 = cb.createQuery(Employee.class);
        Root<Employee> emp1 = query1.from(Employee.class);
        query1.where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2.where(cb.eq(emp2.get("departmentId"), 2));

        String intersectSql = query1.intersect(query2).buildSelectQuery();
        String expectedSql = "select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 1) intersect select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 2)";

        assertEquals(expectedSql, intersectSql);
    }


    @Test
    void testExcept() {
        CriteriaQuery<Employee> query1 = cb.createQuery(Employee.class);
        Root<Employee> emp1 = query1.from(Employee.class);
        query1.where(cb.eq(emp1.get("departmentId"), 1));

        CriteriaQuery<Employee> query2 = cb.createQuery(Employee.class);
        Root<Employee> emp2 = query2.from(Employee.class);
        query2.where(cb.eq(emp2.get("departmentId"), 2));

        String exceptSql = query1.except(query2).buildSelectQuery();

        String expectedSql = "select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 1) except select EMPLOYEES.ENABLED as enabled,EMPLOYEES.FIRST_NAME as firstName,EMPLOYEES.ID as id,EMPLOYEES.LAST_NAME as lastName,EMPLOYEES.SALARY as salary from EMPLOYEES EMPLOYEES where (EMPLOYEES.departmentId = 2)";
        assertEquals(expectedSql, exceptSql);
    }
}