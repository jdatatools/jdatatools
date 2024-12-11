package com.ainouss.datatools.jdatatools.query;

import com.ainouss.datatools.jdatatools.query.expression.Where;
import com.ainouss.datatools.jdatatools.query.join.Join;
import com.ainouss.datatools.jdatatools.query.order.OrderDirection;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ainouss.datatools.jdatatools.query.EntityRegistry.fullResolve;
import static com.ainouss.datatools.jdatatools.util.QueryBuilder.getSelectableFields;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Criteria query builder
 *
 * @param <T> class
 */
public class CriteriaQuery<T> {

    private final Root<T> root;
    private Expression where;
    private final LinkedHashMap<Path<?>, OrderDirection> orderBy = new LinkedHashMap<>();
    private final List<Join<?, ?>> joins = new ArrayList<>();
    private final LinkedHashSet<Selection<?>> selection = new LinkedHashSet<>();
    private final LinkedHashSet<Path<?>> groupBy = new LinkedHashSet<>();
    private Function<String, String> from = table -> table;

    /**
     * Criteria query does not resolve annotations by its own, a map of paths should be passed as a parameter
     *
     * @param javaType java class
     */
    CriteriaQuery(Class<T> javaType) {
        this.root = new Root<>(javaType);
        EntityRegistry.registerClass(javaType);
    }

    /**
     * Multi op selection, select attribute or selection
     *
     * @param paths paths
     * @return this
     */
    public final CriteriaQuery<T> select(Path<?>... paths) {
        if (paths != null) {
            var list = Arrays.stream(paths)
                    .map(Selection::new)
                    .toList();
            selection.addAll(list);
        }
        return this;
    }

    /**
     * Unselect some attributes
     * Should be called after {@see #select()}
     *
     * @param paths paths
     */
    public CriteriaQuery<T> unselect(Path<?>... paths) {
        if (paths != null) {
            var list = Arrays.stream(paths)
                    .map(Selection::new)
                    .toList();
            list.forEach(selection::remove);
        }
        return this;
    }

    /**
     * Select entire root, all columns in that root are selected
     *
     * @param root root
     * @return this
     */
    public final CriteriaQuery<T> select(Root<?> root) {
        var select = EntityRegistry.columns.keySet()
                .stream()
                .filter(path -> path.head.equals(root))
                .peek(path -> path.head.as(root.getAlias()))
                .map(path -> new Selection<>(path.head, path.attribute))
                .toList();
        this.selection.addAll(select);
        return this;
    }

    /**
     * Prefix the table name at runtime. useful to reuse the table name already set in the @Table annotation
     *
     * @param prefix prefix to be added to the table name
     * @return root
     */
    public Root<?> prefix(String prefix) {
        if (prefix != null) {
            this.from = str -> prefix + str;
        }
        return root;
    }

    /**
     * Override table name,
     *
     * @param from table name
     * @return this for chaining
     */
    public CriteriaQuery<T> from(String from) {
        this.from = str -> from;
        return this;
    }

    /**
     * Override table name,
     *
     * @param from table name
     * @return this for chaining
     */
    public CriteriaQuery<T> from(Function<String, String> from) {
        this.from = from;
        return this;
    }

    /**
     * Overload a root object
     *
     * @param from java class
     * @param <R>  class type
     * @return root object
     */
    public <R> Root<R> from(Class<R> from) {
        EntityRegistry.registerClass(from);
        if (from.equals(root.getJavaType())) {
            return (Root<R>) root;
        }
        Root<R> root = new Root<>(from);
        return root;
    }

    /**
     * does only
     *
     * @param from from
     * @return this
     */
    public CriteriaQuery<T> from(Root<?> from) {
        EntityRegistry.registerClass(from.getJavaType());
        return this;
    }

    /**
     * Root
     *
     * @return root
     */

    public Root<T> from() {
        return root;
    }

    /**
     * Where expression
     *
     * @param expression expression
     * @return where
     */
    public CriteriaQuery<T> where(Expression expression) {
        this.where = new Where(expression);
        return this;
    }

    /**
     * Join a table from this root
     *
     * @param join join expression
     * @param <X>  join start
     * @param <Y>  joined root
     * @return join expression
     */
    public <X, Y> CriteriaQuery<T> join(Join<X, Y> join) {
        this.joins.add(join);
        return this;
    }

    /**
     * Order by path
     *
     * @param path  path
     * @param order order direction
     * @return this
     */
    public CriteriaQuery<T> orderBy(Path<?> path, OrderDirection order) {
        this.orderBy.put(path, order);
        return this;
    }

    /**
     * Order by path, default order ASC
     *
     * @param path path
     * @return this for chaining
     */
    public CriteriaQuery<T> orderBy(Path<?> path) {
        this.orderBy.put(path, OrderDirection.ASC);
        return this;
    }

    /**
     * Group by path
     *
     * @param path path
     * @return this for chaining
     */
    public CriteriaQuery<T> groupBy(Path<?> path) {
        this.groupBy.add(path);
        return this;
    }

    /**
     * Select column names with alias, alias is the java field name
     *
     * @return column names
     */
    private String select() {
        checkSelection();
        String select = selection
                .stream()
                .sorted(Comparator.comparing(o -> o.attribute))
                .map(expression -> new StringBuilder(render(expression))
                        .append(expression.getAttribute() != null ? " as " : "")
                        .append(expression.getAttribute() != null ? expression.getAttribute() : "")
                )
                .map(StringBuilder::toString)
                .collect(Collectors.joining(","));
        return defaultIfEmpty(select, " * ");

    }

    /**
     * Where clause
     *
     * @return where clause
     */
    private String where() {
        String render = render(where);
        if (render.trim().isEmpty()) {
            return "";
        }
        return " where " + render;
    }

    /**
     * Render selection
     *
     * @param target selection
     * @return SQL subset
     */
    private String render(Selection<?> target) {
        if (target == null) {
            return "";
        }
        if (target.expression != null) {
            return render(target.expression);
        }
        return EntityRegistry.fullResolve(target);
    }

    /**
     * Render expression to a sql fragment.
     *
     * @param target expression
     * @return sql fragment
     */
    private String render(Expression target) {
        if (target == null) {
            return "";
        }
        String ands = target.and
                .stream()
                .map(this::render)
                .collect(Collectors.joining(" and "));

        String ors = target.or
                .stream()
                .map(this::render)
                .collect(Collectors.joining(" or "));

        String nots = target.not
                .stream()
                .map(this::render)
                .collect(Collectors.joining(" and "));

        var sql = new StringBuilder(EntityRegistry.fullResolve(target.path));
        sql.append(target.sql());

        if (!target.not.isEmpty()) {
            sql.append("(")
                    .append(nots)
                    .append(")");
        }

        if (isNotBlank(ands)) {
            sql.append(sql.isEmpty() ? "" : " and ")
                    .append("(")
                    .append(ands)
                    .append(")");
        }
        if (isNotBlank(ors)) {
            sql.append(sql.isEmpty() ? "" : " or ")
                    .append("(")
                    .append(ors)
                    .append(")");
        }
        return sql.toString();
    }

    /**
     * insert column names
     *
     * @return insert column names
     */
    private String insert() {
        return selection
                .stream()
                .map(EntityRegistry::resolve)
                .collect(Collectors.joining(","));
    }

    /**
     * insert field names
     *
     * @return insert field names
     */
    private String values() {
        return selection
                .stream()
                .map(path -> new StringBuilder(":").append(path.getAttribute()))
                .collect(Collectors.joining(","));
    }

    /**
     * Table alias
     *
     * @return alias
     */
    private String alias(Root<?> root) {
        if (isNotBlank(root.getAlias())) {
            return root.getAlias();
        }
        return sourceTable(root);
    }

    /**
     * Source table name
     *
     * @return table
     */
    private String sourceTable(Root<?> root) {
        String rootTable = EntityRegistry.tables.get(root);
        if (this.from != null) {
            rootTable = this.from.apply(rootTable);
        }
        return root.schema() + rootTable;
    }

    /**
     * select #{fields} from #{sourceTable} #{alias} #{joins} #{where} #{order}
     *
     * @return select query
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
                .append(orderBy())
                .toString()
                .trim()
                .replaceAll("  +", " ");
    }

    /**
     * Source table
     *
     * @return table name
     */
    public String sourceTable() {
        return sourceTable(root);
    }

    /**
     * count query from a criteria, order by is omitted
     *
     * @return count query
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
     * To be implemented
     *
     * @return joins clause
     */
    private String joins() {
        return this.joins.stream()
                .map(join -> new StringBuilder(join.getJoinType().name().toLowerCase())
                        .append(" join ")
                        .append(sourceTable(join.getTarget()))
                        .append(" ")
                        .append(alias(join.getTarget()))
                        .append(join.getOn() != null ? " on " : "")
                        .append(render(join.getOn()))
                        .toString()
                )
                .collect(Collectors.joining(" "));
    }

    /**
     * delete from #{targetTable} #{alias} #{where}
     *
     * @return delete query
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
     * Build insert query with template
     * insert into #{targetTable} (#{columns}) values (#{values})
     * use {@link #select()} to limit the scope of attributes to insert
     *
     * @return named insert query
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
     * Order by clause
     *
     * @return Order by clause
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
     * Group by clause
     *
     * @return Order by clause
     */
    private String groupBy() {
        String orderBy = this.groupBy
                .stream()
                .map(orderEntry -> new StringBuilder(fullResolve(orderEntry))
                        .append(" ")
                )
                .map(StringBuilder::toString)
                .collect(Collectors.joining(","));
        return orderBy.isEmpty() ? "" : " group by " + orderBy;
    }

    /**
     * Selected fields based on criteria selection
     *
     * @return selected fields
     */
    public List<Field> getFields() {
        List<Field> selectableFields = getSelectableFields(root.getJavaType());
        if (this.selection.isEmpty()) {
            return selectableFields;
        }
        return this.selection
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
     * If the selection is empty, select all
     */
    private void checkSelection() {
        if (this.selection.isEmpty()) {
            select(root);
        }
    }
}

