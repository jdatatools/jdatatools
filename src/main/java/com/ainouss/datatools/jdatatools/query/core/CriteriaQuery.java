package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.expression.IdentityExpression;
import com.ainouss.datatools.jdatatools.query.expression.Where;
import com.ainouss.datatools.jdatatools.query.join.Join;
import com.ainouss.datatools.jdatatools.query.order.OrderDirection;
import com.ainouss.datatools.jdatatools.query.registery.EntityRegistry;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ainouss.datatools.jdatatools.query.registery.EntityRegistry.fullResolve;
import static com.ainouss.datatools.jdatatools.util.QueryBuilder.getSelectableFields;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Criteria query builder. This class provides a fluent API for constructing SQL queries
 * in a type-safe manner. It supports various operations such as selecting columns,
 * filtering data with where clauses, joining tables, ordering results, and grouping.
 *
 * @param <T> The type of the entity being queried.
 */
public class CriteriaQuery<T> {

    private final Root<T> root;
    private Expression where = new Where();
    private final Expression having = new IdentityExpression();
    private final LinkedHashMap<Path<?>, OrderDirection> orderBy = new LinkedHashMap<>();
    private final List<Join<?, ?>> joins = new ArrayList<>();
    private final LinkedHashSet<PathExpression<?>> selections = new LinkedHashSet<>();
    private final LinkedHashSet<Path<?>> groupBy = new LinkedHashSet<>();
    private Function<String, String> from = table -> table;

    /**
     * Constructs a new {@code CriteriaQuery} instance.
     * <p>
     * Note: This constructor does not resolve annotations automatically.
     * A map of paths should be provided for annotation resolution.
     *
     * @param javaType The Java class of the entity being queried.
     */
    CriteriaQuery(Class<T> javaType) {
        this.root = new Root<>(javaType);
        EntityRegistry.registerClass(javaType);
    }

    /**
     * Selects multiple attributes or expressions.
     *
     * @param paths The paths representing the attributes or expressions to select.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public final CriteriaQuery<T> select(Path<?>... paths) {
        if (paths != null) {
            var list = Arrays.stream(paths)
                    .map(PathExpression::new)
                    .toList();
            selections.addAll(list);
        }
        return this;
    }

    /**
     * Selects multiple attributes or expressions.
     *
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    @SafeVarargs
    public final CriteriaQuery<T> select(Function<Root<T>, Path<?>>... functions) {
        if (functions != null) {
            var list = Arrays.stream(functions)
                    .map(select -> select.apply(root))
                    .map(PathExpression::new)
                    .toList();
            selections.addAll(list);
        }
        return this;
    }

    /**
     * Unselects specific attributes from the selection. This method should be called
     * after {@link #select()} to remove attributes from the previously defined selection.
     *
     * @param paths The paths representing the attributes to unselect.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> unselect(Path<?>... paths) {
        if (paths != null) {
            var list = Arrays.stream(paths)
                    .map(PathExpression::new)
                    .toList();
            list.forEach(selections::remove);
        }
        return this;
    }

    /**
     * Selects all columns from the specified root entity.
     *
     * @param root The root entity from which to select all columns.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public final CriteriaQuery<T> select(Root<?> root) {
        var select = EntityRegistry.columns.keySet()
                .stream()
                .filter(path -> path.head.equals(root))
                .peek(path -> path.head.as(root.getAlias()))
                .map(path -> new PathExpression<>(path.head, path.attribute))
                .toList();
        this.selections.addAll(select);
        return this;
    }

    /**
     * Prefixes the table name with the given string. This is useful for reusing
     * the table name already defined in the {@code @Table} annotation.
     *
     * @param prefix The prefix to be added to the table name.
     * @return The root entity.
     */
    public Root<?> prefix(String prefix) {
        if (prefix != null) {
            this.from = str -> prefix + str;
        }
        return root;
    }

    /**
     * Overrides the table name with the specified string.
     *
     * @param from The new table name.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> from(String from) {
        this.from = str -> from;
        return this;
    }

    /**
     * Overrides the table name using the provided function. The function takes the
     * original table name as input and returns the modified table name.
     *
     * @param from The function to apply to the table name.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> from(Function<String, String> from) {
        this.from = from;
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
        if (from.equals(root.getJavaType())) {
            return (Root<R>) root;
        }
        return new Root<>(from);
    }

    /**
     * Sets the root object without modifying the existing root.
     *
     * @param from The new root object.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> from(Root<?> from) {
        EntityRegistry.registerClass(from.getJavaType());
        return this;
    }

    /**
     * Returns the root entity of this query.
     *
     * @return The root entity.
     */
    public Root<T> from() {
        return root;
    }

    /**
     * Adds a where clause to the query.
     *
     * @param expression The expression for the where clause.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> where(Expression expression) {
        this.where = new Where(expression);
        return this;
    }

    /**
     * Joins a table to this query.
     *
     * @param join The join expression.
     * @param <X>  The type of the starting entity in the join.
     * @param <Y>  The type of the joined entity.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public <X, Y> CriteriaQuery<T> join(Join<X, Y> join) {
        this.joins.add(join);
        return this;
    }

    /**
     * Adds an order by clause to the query.
     *
     * @param path  The path representing the attribute to order by.
     * @param order The order direction (ascending or descending).
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> orderBy(Path<?> path, OrderDirection order) {
        this.orderBy.put(path, order);
        return this;
    }

    /**
     * Adds a Having clause to the query.
     *
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> having(Expression expression) {
        this.having.and(expression);
        return this;
    }

    /**
     * Adds an order by clause to the query with the default ascending order.
     *
     * @param path The path representing the attribute to order by.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> orderBy(Path<?> path) {
        this.orderBy.put(path, OrderDirection.ASC);
        return this;
    }

    /**
     * Adds a group by clause to the query.
     *
     * @param path The path representing the attribute to group by.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public CriteriaQuery<T> groupBy(Path<?> path) {
        this.groupBy.add(path);
        return this;
    }

    /**
     * Generates the select clause of the SQL query. Column names are aliased with
     * their corresponding Java field names.
     *
     * @return The select clause of the SQL query.
     */
    private String select() {
        checkSelection();
        String select = selections
                .stream()
                .sorted(Comparator.comparing(o -> o.attribute))
                .map(expression -> new StringBuilder(expression.render())
                        .append(expression.getAttribute() != null ? " as " : "")
                        .append(expression.getAttribute() != null ? expression.getAttribute() : "")
                )
                .map(StringBuilder::toString)
                .collect(Collectors.joining(","));
        return defaultIfEmpty(select, " * ");

    }

    /**
     * Generates the where clause of the SQL query.
     *
     * @return The where clause of the SQL query.
     */
    private String where() {
        String render = where.render();
        if (render.equals(where.sql()) || render.equals(" where ()")) {
            return "";
        }
        return render;
    }

    /**
     * Generates the column names for an insert query.
     *
     * @return The comma-separated list of column names.
     */
    private String insert() {
        return selections
                .stream()
                .map(EntityRegistry::resolve)
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
        String rootTable = EntityRegistry.tables.get(root);
        if (this.from != null) {
            rootTable = this.from.apply(rootTable);
        }
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
        return new StringBuilder().append("select ")
                .append(select())
                .append(" from ")
                .append(sourceTable(root))
                .append(" ")
                .append(alias(root))
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
                .toString()
                .trim()
                .replaceAll("  +", " ");
    }

    private String having() {
        String render = this.having.render();
        if (render.isEmpty()) {
            return "";
        }
        return " having " + render;
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
                .append(sourceTable(root))
                .append(" ")
                .append(alias(root))
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
                .map(join -> new StringBuilder(join.getJoinType().name().toLowerCase())
                        .append(" join ")
                        .append(sourceTable(join.getTarget()))
                        .append(" ")
                        .append(alias(join.getTarget()))
                        .append(join.getOn() != null ? " on " : "")
                        .append(join.getOn() != null ? join.getOn().render() : "")
                        .toString()
                )
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
                .append(sourceTable(root))
                .append(" ")
                .append(alias(root))
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
        checkSelection();
        return new StringBuilder().append("insert into ")
                .append(sourceTable(root))
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
        String orderBy = this.orderBy.entrySet()
                .stream()
                .map(orderEntry -> new StringBuilder(fullResolve(orderEntry.getKey()))
                        .append(" ")
                        .append(orderEntry.getValue())
                )
                .map(StringBuilder::toString)
                .collect(Collectors.joining(","));
        return orderBy.isEmpty() ? "" : " order by " + orderBy;
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
    public List<Field> getFields() {
        List<Field> selectableFields = getSelectableFields(root.getJavaType());
        if (this.selections.isEmpty()) {
            return selectableFields;
        }
        return this.selections
                .stream()
                .map(select -> {
                    try {
                        return select.head.getJavaType().getDeclaredField(select.attribute);
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

    /**
     * Ensures that at least one column is selected in the query. If no selection
     * is explicitly defined, all columns from the root entity are selected.
     */
    private void checkSelection() {
        if (this.selections.isEmpty()) {
            select(root);
        }
    }

    public CriteriaQuery<T> as(String tbl) {
        this.root.as(tbl);
        return this;
    }

}