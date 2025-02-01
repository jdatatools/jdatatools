package com.ainouss.jdatatools.query.core;

import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration
import com.ainouss.jdatatools.query.join.Join;
import com.ainouss.jdatatools.query.order.Order;
import com.ainouss.jdatatools.query.order.OrderDirection;
import com.ainouss.jdatatools.query.registery.EntityRegistry;
import com.ainouss.jdatatools.query.setoperation.*;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.ainouss.jdatatools.query.registery.EntityRegistry.fullResolve;
import static com.ainouss.jdatatools.query.util.DataUtils.defaultIfEmpty;
import static com.ainouss.jdatatools.query.util.DataUtils.isNotBlank;


/**
 * Criteria query builder. This class provides a fluent API for constructing SQL queries
 * in a type-safe manner. It supports various operations such as selecting columns,
 * filtering data with where clauses, joining tables, ordering results, and grouping.
 *
 * @param <T> The type of the entity being queried.
 */
public class CriteriaQuery<T> {

    @Getter
    protected Class<T> resultType;
    protected final LinkedHashSet<Selectable> selections = new LinkedHashSet<>();
    protected final LinkedHashSet<Source> froms = new LinkedHashSet<>();
    protected final SimpleExpression where; // Dialect Integration
    protected final SimpleExpression having; // Dialect Integration
    protected final LinkedHashSet<Order> orderBy = new LinkedHashSet<>();
    protected final List<Join<?, ?>> joins = new ArrayList<>();
    protected final LinkedHashSet<Expression> groupBy = new LinkedHashSet<>();
    protected final List<SetOperation> unions = new ArrayList<>();
    protected final Pagination pagination; // Dialect Integration
    private final CriteriaBuilder criteriaBuilder;

    /**
     * Constructs a new {@code CriteriaQuery} instance.
     * <p>
     * Note: This constructor does not resolve annotations automatically.
     * A map of paths should be provided for annotation resolution.
     *
     * @param javaType The Java class of the entity being queried.
     */
    CriteriaQuery(Class<T> javaType, CriteriaBuilder criteriaBuilder) {
        EntityRegistry.registerClass(javaType);
        this.resultType = javaType;
        this.criteriaBuilder = criteriaBuilder;
        this.where = new SimpleExpression(criteriaBuilder.getSqlDialect()); // Dialect Integration
        this.having = new SimpleExpression(criteriaBuilder.getSqlDialect());// Dialect Integration
        this.pagination = new Pagination(criteriaBuilder.getSqlDialect()); // Dialect Integration
    }

    protected CriteriaQuery() {
        // For CTEs - no dialect initially, dialect will be inherited from root query
        this.criteriaBuilder = null; // CTEs don't have a CriteriaBuilder initially
        this.where = new SimpleExpression();
        this.having = new SimpleExpression();
        this.pagination = new Pagination();
    }


    public CriteriaQuery<T> from(Source source, Source... sources) {
        if (source == null && sources == null) {
            throw new RuntimeException("no source specified");
        }
        this.froms.clear();
        this.froms.add(source);
        if (sources != null) {
            this.froms.addAll(Arrays.asList(sources));
        }

        return this;
    }

    /**
     * Selects multiple attributes or expressions.
     *
     * @param selectables The paths representing the attributes or expressions to select.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public final CriteriaQuery<T> select(Selectable... selectables) {
        if (selectables == null) {
            return this;
        }
        selections.clear();
        var list = Arrays.stream(selectables)
                .toList();
        selections.addAll(list);
        return this;
    }

    /**
     * Selects all columns from the specified root entity.
     *
     * @param selected The root entity from which to select all columns.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public final CriteriaQuery<T> select(Source selected) {
        var select = EntityRegistry.paths.keySet()
                .stream()
                .filter(path -> path.head.equals(selected))
                .peek(path -> path.head.as(selected.getAlias()))
                .toList();
        this.selections.addAll(select);
        return this;
    }


    /**
     * Overloads the root object with a new entity type.
     *
     * @param from The Java class of the new root entity.
     * @param <R>  The type of the new root entity.
     * @return The new root object.
     */
    public <R> Root<R> from(Class<R> from) {
        EntityRegistry.registerClass(from);
        Root<R> root = new Root<>(from);
        this.froms.add(root);
        return root;
    }

    /**
     * Sets the root object without modifying the existing root.
     *
     * @param from The new root object.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> from(Root<?> from) {
        this.froms.add(from);
        return this;
    }


    private String froms() {
        return froms.stream()
                .map(from -> from.getName() + " " + from.getAlias())
                .collect(Collectors.joining(","));
    }

    private String into() {
        return froms.stream()
                .map(Source::getName)
                .collect(Collectors.joining(","));
    }

    /**
     * Adds a where clause to the query.
     *
     * @param expression The expression for the where clause.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> where(Expression expression) {
        this.where.with(expression);
        return this;
    }

    /**
     * Adds an order by clause to the query.
     *
     * @param path  The attribute representing the attribute to order by.
     * @param order The order direction (ascending or descending).
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> orderBy(Selectable path, OrderDirection order) {
        this.orderBy.add(new Order(path, order));
        return this;
    }

    /**
     * Adds a Having clause to the query.
     *
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> having(Expression expression) {
        this.having.with(expression);
        return this;
    }

    /**
     * Adds an order by clause to the query with the default ascending order.
     *
     * @param path The path representing the attribute to order by.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> orderBy(Path<?> path) {
        this.orderBy.add(new Order(path));
        return this;
    }

    public CriteriaQuery<T> orderBy(Order... orders) {
        if (orders == null) {
            return this;
        }
        this.orderBy.addAll(Arrays.asList(orders));
        return this;
    }

    /**
     * Adds a group by clause to the query.
     *
     * @param expression The path representing the attribute to group by.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> groupBy(Expression expression) {
        this.groupBy.add(expression);
        return this;
    }

    public CriteriaQuery<T> limit(Integer limit) {
        this.pagination.setLimit(limit);
        return this;
    }

    public CriteriaQuery<T> offset(Integer offset) {
        this.pagination.setOffset(offset);
        return this;
    }

    public CriteriaQuery<T> union(CriteriaQuery<?> other) {
        this.unions.add(new Union(other));
        return this;
    }

    public CriteriaQuery<T> unionAll(CriteriaQuery<?> other) {
        this.unions.add(new UnionAll(other));
        return this;
    }

    public CriteriaQuery<T> intersect(CriteriaQuery<?> other) {
        this.unions.add(new Intersect(other));
        return this;
    }

    public CriteriaQuery<T> except(CriteriaQuery<?> other) {
        this.unions.add(new Except(other));
        return this;
    }

    /**
     * Generates the select clause of the SQL query. Column names are aliased with
     * their corresponding Java field names.
     *
     * @return The select clause of the SQL query.
     */
    private String select() {
        String select = selections
                .stream()
                .sorted()
                .map(this::toSql)
                .collect(Collectors.joining(","));
        return defaultIfEmpty(select, " * ");

    }

    private String update() {
        return this.selections
                .stream()
                .sorted()
                .map(selectable -> {
                    if (selectable instanceof Path<?> path) {
                        return path;
                    }
                    throw new RuntimeException("not a path");
                })
                .map(path -> EntityRegistry.paths.get(path) + " = :" + path.getAttribute())
                .collect(Collectors.joining(", "));

    }

    private String toSql(Selectable attr) {
        String str = attr.toSql();
        if (isNotBlank(attr.getAlias())) {
            return str + " as " + attr.getAlias();
        }
        return str;
    }

    /**
     * Generates the where clause of the SQL query.
     *
     * @return The where clause of the SQL query.
     */
    private String where() {
        String sql = where.toSql();
        if (sql.isEmpty()) {
            return "";
        }
        return " where (" + sql + ")";
    }

    /**
     * Generates the column names for an insert query.
     *
     * @return The comma-separated list of column names.
     */
    private String insert() {
        return selections
                .stream()
                .map(selectable -> {
                    if (selectable instanceof Path<?> path) {
                        return path;
                    }
                    throw new RuntimeException("not a path");
                })
                .map(EntityRegistry::resolvePath)
                .collect(Collectors.joining(","));
    }

    /**
     * Generates the values placeholder for an insert query.
     *
     * @return The comma-separated list of values placeholders.
     */
    private String values() {
        return selections
                .stream()
                .map(selectable -> {
                    if (selectable instanceof Path<?> path) {
                        return path;
                    }
                    throw new RuntimeException("not a path");
                })
                .map(path -> new StringBuilder(":").append(path.getAttribute()))
                .collect(Collectors.joining(","));
    }

    /**
     * Returns the alias of the given root entity. If no alias is explicitly set,
     * the source table name is used as the alias.
     *
     * @param root The root entity.
     * @return The alias of the root entity.
     */
    private String alias(Root<?> root) {
        if (isNotBlank(root.getAlias())) {
            return root.getAlias();
        }
        return sourceTable(root);
    }

    /**
     * Returns the source table name for the given root entity. This method takes
     * into account any prefixes or custom table name mappings applied to the query.
     *
     * @param root The root entity.
     * @return The source table name.
     */
    private String sourceTable(Root<?> root) {
        String rootTable = EntityRegistry.roots.get(root);
        return root.schema() + rootTable;
    }

    /**
     * Builds a select query based on the criteria defined in this query builder.
     * The query includes the select clause, from clause, joins, where clause,
     * group by clause, and order by clause.
     *
     * @return The complete select query.
     */
    public String buildSelectQuery() {
        checkSelection();
        if (unions.isEmpty() || orderBy.isEmpty() && pagination.isVoid()) {
            return buildSimpleSelectQuery();
        }
        return buildNestedSelectQuery();
    }

    public String buildNamedUpdateQuery() {
        checkSelection();
        return new StringBuilder().append("update ")
                .append(froms())
                .append(" set ")
                .append(update())
                .toString();
    }

    protected void checkSelection() {
        if (selections.isEmpty()) {
            froms.forEach(this::select);
        }
    }

    private String buildSimpleSelectQuery() {
        return new StringBuilder().append("select ")
                .append(select())
                .append(" from ")
                .append(froms())
                .append(" ")
                .append(joins())
                .append(" ")
                .append(where())
                .append(" ")
                .append(groupBy())
                .append(" ")
                .append(having())
                .append(" ")
                .append(orderBy())
                .append(" ")
                .append(unions())
                .append(" ")
                .append(limitOffset())
                .toString()
                .trim()
                .replaceAll("  +", " ");
    }

    private String buildNestedSelectQuery() {
        Pagination pagination1 = Pagination.from(this.pagination);
        Subquery subquery = new Subquery(this, criteriaBuilder.getSqlDialect()); // Dialect Integration
        subquery.setAlias("nested_query");
        var nq = new CriteriaQuery<>(this.resultType, criteriaBuilder); // Dialect Integration
        nq.from(subquery);
        this.orderBy.forEach((order) -> {
            Selectable p = new Path<>("nested_query", order.getColumn().getAlias());
            nq.orderBy(p, order.getDirection());
        });
        nq.pagination.apply(pagination1);
        subquery.cr().orderBy.clear();
        subquery.cr().pagination.clear();
        //
        return nq.buildSelectQuery();
    }

    private String unions() {
        StringBuilder sb = new StringBuilder();
        for (SetOperation union : unions) {
            sb.append(" ").append(union.toSql());
        }
        return sb.toString();
    }

    private String having() {
        String toString = this.having.toSql();
        if (toString.isEmpty()) {
            return "";
        }
        return " having " + toString;
    }

    /**
     * Builds a count query based on the criteria defined in this query builder.
     * The query counts all rows matching the criteria, excluding the order by clause.
     *
     * @return The count query.
     */
    public String buildCountQuery() {
        return new StringBuilder().append("select count(*)")
                .append(" from ")
                .append(froms())
                .append(" ")
                .append(" ")
                .append(joins())
                .append(" ")
                .append(where())
                .append(" ")
                .append(groupBy())
                .append(" ")
                .toString()
                .trim()
                .replaceAll("  +", " ");
    }

    /**
     * Generates the joins clause of the SQL query.
     *
     * @return The joins clause of the SQL query.
     */
    private String joins() {
        return this.joins.stream()
                .map(Join::toSql)
                .collect(Collectors.joining(" "));
    }

    /**
     * Builds a delete query based on the criteria defined in this query builder.
     * The query deletes all rows matching the where clause.
     *
     * @return The delete query.
     */
    public String buildDeleteQuery() {
        return new StringBuilder().append("delete from ")
                .append(froms())
                .append(" ")
                .append(where())
                .append(" ")
                .toString()
                .trim()
                .replaceAll("  +", " ");
    }

    /**
     * Builds an insert query with named parameters. The query inserts data into
     * the specified columns. Use {@link #select()} to limit the scope of attributes
     * to be inserted.
     *
     * @return The parameterized insert query.
     */
    public String buildInsertQuery() {
        return new StringBuilder().append("insert into ")
                .append(into())
                .append(" (")
                .append(insert())
                .append(")")
                .append(" values ")
                .append("(")
                .append(values())
                .append(")")
                .toString()
                .trim()
                .replaceAll("  +", " ");
    }

    /**
     * Generates the order by clause of the SQL query.
     *
     * @return The order by clause of the SQL query.
     */
    private String orderBy() {
        String orderBy = this.orderBy
                .stream()
                .map(order -> new StringBuilder(fullResolve(order.getColumn()))
                        .append(" ")
                        .append(order.getDirection())
                )
                .map(StringBuilder::toString)
                .collect(Collectors.joining(","));
        return orderBy.isEmpty() ? "" : " order by " + orderBy;
    }

    private String limitOffset() {
        return pagination.render();
    }

    /**
     * Generates the group by clause of the SQL query.
     *
     * @return The group by clause of the SQL query.
     */
    private String groupBy() {
        String groupByClause = this.groupBy
                .stream()
                .map(EntityRegistry::fullResolve)
                .collect(Collectors.joining(","));
        return groupByClause.isEmpty() ? "" : " group by " + groupByClause;
    }

    /**
     * Returns a list of fields selected in this query. If no specific selection
     * is defined, all selectable fields of the root entity are returned.
     *
     * @return The list of selected fields.
     */
    public List<FieldMetaData> getFields() {
        return this.selections
                .stream()
                .map(select -> {
                    FieldMetaData field = new FieldMetaData();
                    field.setLabel(select.getAlias());
                    field.setColumn(select.getColumn());
                    try {
                        Field declaredField = this.getResultType().getDeclaredField(select.getAlias());
                        field.setJavaType(declaredField.getType());
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                    return field;
                })
                .sorted(Comparator.comparing(FieldMetaData::getLabel))
                .collect(Collectors.toList());
    }

    public LinkedHashSet<Selectable> getSelect() {
        return selections;
    }
}