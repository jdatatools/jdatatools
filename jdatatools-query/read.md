# JDataTools Query Framework

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A fluent SQL query builder for Java with JPA integration, dialect support, and advanced SQL features.

## Features ✨

- **JPA Annotation Support** - Map entities using `@Table` and `@Column`
- **Fluent API** - Type-safe query construction
- **Full SQL Coverage**:
    - WHERE clauses with `eq()`, `like()`, `between()`, etc.
    - JOINs (INNER, LEFT, RIGHT, FULL, CROSS)
    - Aggregations (`count()`, `sum()`, `avg()`)
    - Subqueries and CTEs
    - Set operations (`UNION`, `INTERSECT`, `EXCEPT`)
    - Window functions (`OVER()`, `PARTITION BY`)
    - Case statements
- **Multi-Dialect Support** - MySQL, PostgreSQL, SQL Server, and ANSI SQL
- **Spring Integration** - Works seamlessly with Spring projects

## Installation 📦

Add to your project via Maven:

```xml
<dependency>
    <groupId>com.ainouss</groupId>
    <artifactId>jdatatools-query</artifactId>
    <version>1.0.0</version>
</dependency>