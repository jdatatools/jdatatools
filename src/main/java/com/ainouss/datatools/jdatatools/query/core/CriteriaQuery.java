package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.expression.IdentityExpression;
import com.ainouss.datatools.jdatatools.query.expression.Where;
import com.ainouss.datatools.jdatatools.query.join.Join;
import com.ainouss.datatools.jdatatools.query.order.OrderDirection;
import com.ainouss.datatools.jdatatools.query.registery.EntityRegistry;
import com.ainouss.datatools.jdatatools.query.union.Except;
import com.ainouss.datatools.jdatatools.query.union.Intersect;
import com.ainouss.datatools.jdatatools.query.union.Union;
import com.ainouss.datatools.jdatatools.query.union.UnionAll;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.*;
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

    @Getter
    protected final Root<T> root;
    protected final LinkedHashSet<Selectable> selections = new LinkedHashSet<>();
    protected final List<Source> froms = new ArrayList<>();
    private final Where where = new Where();
    protected final IdentityExpression having = new IdentityExpression();
    protected final LinkedHashMap<Path<?>, OrderDirection> orderBy = new LinkedHashMap<>();
    protected final List<Join<?, ?>> joins = new ArrayList<>();
    protected final LinkedHashSet<Path<?>> groupBy = new LinkedHashSet<>();
    protected final List<SetOperation> unions = new ArrayList<>();
    protected final Page page = new Page();
    protected boolean noAlias = false;


    /**
     * Constructs a new {@code CriteriaQuery} instance.
     * <p>
     * Note: This constructor does not resolve annotations automatically.
     * A map of paths should be provided for annotation resolution.
     *
     * @param javaType The Java class of the entity being queried.
     */
    CriteriaQuery(Class<T> javaType) {
        EntityRegistry.registerClass(javaType);
        this.root = new Root<>(javaType);
        this.froms.add(root);
    }

    public CriteriaQuery<T> from(Source from) {
        this.froms.clear();
        this.froms.add(from);
        return this;
    }

    /**
     * Selects multiple attributes or expressions.
     *
     * @param selectables The paths representing the attributes or expressions to select.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public final CriteriaQuery<T> select(Selectable... selectables) {
        if (selectables != null) {
            var list = Arrays.stream(selectables)
                    .toList();
            selections.addAll(list);
        }
        return this;
    }

    /**
     * Selects all columns from the specified root entity.
     *
     * @param selected The root entity from which to select all columns.
     * @return This {@code CriteriaQuery} instance for method chaining.
     */
    public final CriteriaQuery<T> select(Root<?> selected) {
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


    private String froms() {
        return froms.stream()
                .map(from -> from.render() + " " + from.getAlias())
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

    public CriteriaQuery<T> limit(Integer limit) {
        this.page.limit(limit);
        return this;
    }

    public CriteriaQuery<T> offset(Integer offset) {
        this.page.offset(offset);
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
        checkSelection();
        String select = selections
                .stream()
                .sorted(Comparator.comparing(Selectable::column))
                .map(this::toSql)
                .collect(Collectors.joining(","));
        return defaultIfEmpty(select, " * ");

    }

    private String toSql(Selectable attr) {
        String str = attr.toSql();
        if (noAlias) {
            return str;
        }
        if (isNotBlank(attr.column())) {
            return str + " as " + attr.column();
        }
        return str;
    }

    /**
     * Generates the where clause of the SQL query.
     *
     * @return The where clause of the SQL query.
     */
    private String where() {
        String sql = where.sql();
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
                .map(selectable -> new Path<>(selectable.root(), selectable.column()))
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
                .map(path -> new StringBuilder(":").append(path.column()))
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
        if (unions.isEmpty() || orderBy.isEmpty() && page.isEmpty()) {
            return buildSimpleSelectQuery();
        }
        return buildNestedSelectQuery();
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
        this.noAlias = true;
        LinkedHashMap<Path<?>, OrderDirection> order = new LinkedHashMap<>(this.orderBy);
        LinkedHashSet<Selectable> selections1 = new LinkedHashSet<>(this.selections);
        Page page1 = Page.from(this.page);
        Subquery from = new Subquery(this);
        var nq = new CriteriaQuery<>(this.root.getJavaType());
        nq.from(from);
        nq.orderBy.putAll(order);
        nq.page.apply(page1);
        nq.selections.addAll(selections1);
        from.cr().orderBy.clear();
        from.cr().page.clear();
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
        String toString = this.having.render();
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
                .map(Join::render)
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

    private String limitOffset() {
        return page.render();
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
                        return select.root().getJavaType().getDeclaredField(select.column());
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

    public LinkedHashSet<Selectable> getSelect() {
        return selections;
    }
}