package com.mira.jpa2;

import com.mira.jpa2.hibernate.ContainsFunction;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.predicate.CompoundPredicate;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/**
 * Строит запрос. Не является потокобезопасным
 */
public abstract class QueryBuilder<T> implements CriteriaBuilder {
  protected CriteriaQuery query;
  protected Root<T> root;
  protected CriteriaBuilder builder;
  protected boolean cache;

  public boolean isCache() {
    return cache;
  }

  public void setCache(boolean cache) {
    this.cache = cache;
  }

  @Override
  public <T> CriteriaUpdate<T> createCriteriaUpdate(Class<T> targetEntity) {
    return builder.createCriteriaUpdate(targetEntity);
  }

  @Override
  public <T> CriteriaDelete<T> createCriteriaDelete(Class<T> targetEntity) {
    return builder.createCriteriaDelete(targetEntity);
  }

  @Override
  public <X, T, V extends T> Join<X, V> treat(Join<X, T> join, Class<V> type) {
    return builder.treat(join, type);
  }

  @Override
  public <X, T, E extends T> CollectionJoin<X, E> treat(CollectionJoin<X, T> join, Class<E> type) {
    return builder.treat(join, type);
  }

  @Override
  public <X, T, E extends T> SetJoin<X, E> treat(SetJoin<X, T> join, Class<E> type) {
    return builder.treat(join, type);
  }

  @Override
  public <X, T, E extends T> ListJoin<X, E> treat(ListJoin<X, T> join, Class<E> type) {
    return builder.treat(join, type);
  }

  @Override
  public <X, K, T, V extends T> MapJoin<X, K, V> treat(MapJoin<X, K, T> join, Class<V> type) {
    return builder.treat(join, type);
  }

  @Override
  public <X, T extends X> Path<T> treat(Path<X> path, Class<T> type) {
    return builder.treat(path, type);
  }

  @Override
  public <X, T extends X> Root<T> treat(Root<X> root, Class<T> type) {
    return builder.treat(root, type);
  }

  /**
   * Подготавливает запрос для выполения. В частности формирует условия where и orderBy
   *
   * @param query   запрос
   * @param root    корень запроса
   * @param builder билдер
   * @return подготовленный запрос
   */
  public CriteriaQuery build(CriteriaQuery query, Root<T> root, CriteriaBuilder builder) {
    this.query = query;
    this.root = root;
    this.builder = builder;
    build();
    return this.query;
  }

  abstract protected void build();

  @Override
  public CriteriaQuery<Object> createQuery() {
    return builder.createQuery();
  }

  @Override
  public <T> CriteriaQuery<T> createQuery(Class<T> resultClass) {
    return builder.createQuery(resultClass);
  }

  @Override
  public CriteriaQuery<Tuple> createTupleQuery() {
    return builder.createTupleQuery();
  }

  @Override
  public <Y> CompoundSelection<Y> construct(Class<Y> resultClass, Selection<?>... selections) {
    return builder.construct(resultClass, selections);
  }

  @Override
  public CompoundSelection<Tuple> tuple(Selection<?>... selections) {
    return builder.tuple(selections);
  }

  @Override
  public CompoundSelection<Object[]> array(Selection<?>... selections) {
    return builder.array(selections);
  }

  @Override
  public Order asc(Expression<?> x) {
    return builder.asc(x);
  }

  @Override
  public Order desc(Expression<?> x) {
    return builder.desc(x);
  }

  @Override
  public <N extends Number> Expression<Double> avg(Expression<N> x) {
    return builder.avg(x);
  }

  @Override
  public <N extends Number> Expression<N> sum(Expression<N> x) {
    return builder.sum(x);
  }

  @Override
  public Expression<Long> sumAsLong(Expression<Integer> x) {
    return builder.sumAsLong(x);
  }

  @Override
  public Expression<Double> sumAsDouble(Expression<Float> x) {
    return builder.sumAsDouble(x);
  }

  @Override
  public <N extends Number> Expression<N> max(Expression<N> x) {
    return builder.max(x);
  }

  @Override
  public <N extends Number> Expression<N> min(Expression<N> x) {
    return builder.min(x);
  }

  @Override
  public <X extends Comparable<? super X>> Expression<X> greatest(Expression<X> x) {
    return builder.greatest(x);
  }

  @Override
  public <X extends Comparable<? super X>> Expression<X> least(Expression<X> x) {
    return builder.least(x);
  }

  @Override
  public Expression<Long> count(Expression<?> x) {
    return builder.count(x);
  }

  @Override
  public Expression<Long> countDistinct(Expression<?> x) {
    return builder.countDistinct(x);
  }

  @Override
  public Predicate exists(Subquery<?> subquery) {
    return builder.exists(subquery);
  }

  @Override
  public <Y> Expression<Y> all(Subquery<Y> subquery) {
    return builder.all(subquery);
  }

  @Override
  public <Y> Expression<Y> some(Subquery<Y> subquery) {
    return builder.some(subquery);
  }

  @Override
  public <Y> Expression<Y> any(Subquery<Y> subquery) {
    return builder.any(subquery);
  }

  @Override
  public Predicate and(Expression<Boolean> x, Expression<Boolean> y) {
    return x != null ? (y != null ? builder.and(x, y) : toPredicate(x)) : toPredicate(y);
  }

  protected Predicate toPredicate(Expression<Boolean> expression) {
    return new CompoundPredicate((CriteriaBuilderImpl) builder, Predicate.BooleanOperator.AND, Collections.singletonList(expression));
  }

  @Override
  public Predicate and(Predicate... restrictions) {
    return builder.and(removeNulls(restrictions));
  }

  private Predicate[] removeNulls(Predicate[] restrictions) {
    return Arrays.stream(restrictions).filter(x -> x != null).toArray(Predicate[]::new);
  }

  @Override
  public Predicate or(Expression<Boolean> x, Expression<Boolean> y) {
    return x != null ? (y != null ? builder.or(x, y) : toPredicate(x)) : toPredicate(y);
  }

  @Override
  public Predicate or(Predicate... restrictions) {
    return builder.or(removeNulls(restrictions));
  }

  @Override
  public Predicate not(Expression<Boolean> restriction) {
    return builder.not(restriction);
  }

  @Override
  public Predicate conjunction() {
    return builder.conjunction();
  }

  @Override
  public Predicate disjunction() {
    return builder.disjunction();
  }

  @Override
  public Predicate isTrue(Expression<Boolean> x) {
    return builder.isTrue(x);
  }

  @Override
  public Predicate isFalse(Expression<Boolean> x) {
    return builder.isFalse(x);
  }

  @Override
  public Predicate isNull(Expression<?> x) {
    return builder.isNull(x);
  }

  @Override
  public Predicate isNotNull(Expression<?> x) {
    return builder.isNotNull(x);
  }

  @Override
  public Predicate equal(Expression<?> x, Expression<?> y) {
    return builder.equal(x, y);
  }

  @Override
  public Predicate equal(Expression<?> x, Object y) {
    return builder.equal(x, y);
  }

  @Override
  public Predicate notEqual(Expression<?> x, Expression<?> y) {
    return builder.notEqual(x, y);
  }

  @Override
  public Predicate notEqual(Expression<?> x, Object y) {
    return builder.notEqual(x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate greaterThan(Expression<? extends Y> x, Expression<? extends Y> y) {
    return builder.greaterThan(x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate greaterThan(Expression<? extends Y> x, Y y) {
    return builder.greaterThan(x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(Expression<? extends Y> x, Expression<? extends Y> y) {
    return builder.greaterThanOrEqualTo(x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(Expression<? extends Y> x, Y y) {
    return builder.greaterThanOrEqualTo(x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate lessThan(Expression<? extends Y> x, Expression<? extends Y> y) {
    return builder.lessThan(x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate lessThan(Expression<? extends Y> x, Y y) {
    return builder.lessThan(x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(Expression<? extends Y> x, Expression<? extends Y> y) {
    return builder.lessThanOrEqualTo(x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(Expression<? extends Y> x, Y y) {
    return builder.lessThanOrEqualTo(x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate between(Expression<? extends Y> v, Expression<? extends Y> x, Expression<? extends Y> y) {
    return builder.between(v, x, y);
  }

  @Override
  public <Y extends Comparable<? super Y>> Predicate between(Expression<? extends Y> v, Y x, Y y) {
    return builder.between(v, x, y);
  }

  @Override
  public Predicate gt(Expression<? extends Number> x, Expression<? extends Number> y) {
    return builder.gt(x, y);
  }

  @Override
  public Predicate gt(Expression<? extends Number> x, Number y) {
    return builder.gt(x, y);
  }

  @Override
  public Predicate ge(Expression<? extends Number> x, Expression<? extends Number> y) {
    return builder.ge(x, y);
  }

  @Override
  public Predicate ge(Expression<? extends Number> x, Number y) {
    return builder.ge(x, y);
  }

  @Override
  public Predicate lt(Expression<? extends Number> x, Expression<? extends Number> y) {
    return builder.lt(x, y);
  }

  @Override
  public Predicate lt(Expression<? extends Number> x, Number y) {
    return builder.lt(x, y);
  }

  @Override
  public Predicate le(Expression<? extends Number> x, Expression<? extends Number> y) {
    return builder.le(x, y);
  }

  @Override
  public Predicate le(Expression<? extends Number> x, Number y) {
    return builder.le(x, y);
  }

  @Override
  public <N extends Number> Expression<N> neg(Expression<N> x) {
    return builder.neg(x);
  }

  @Override
  public <N extends Number> Expression<N> abs(Expression<N> x) {
    return builder.abs(x);
  }

  @Override
  public <N extends Number> Expression<N> sum(Expression<? extends N> x, Expression<? extends N> y) {
    return builder.sum(x, y);
  }

  @Override
  public <N extends Number> Expression<N> sum(Expression<? extends N> x, N y) {
    return builder.sum(x, y);
  }

  @Override
  public <N extends Number> Expression<N> sum(N x, Expression<? extends N> y) {
    return builder.sum(x, y);
  }

  @Override
  public <N extends Number> Expression<N> prod(Expression<? extends N> x, Expression<? extends N> y) {
    return builder.prod(x, y);
  }

  @Override
  public <N extends Number> Expression<N> prod(Expression<? extends N> x, N y) {
    return builder.prod(x, y);
  }

  @Override
  public <N extends Number> Expression<N> prod(N x, Expression<? extends N> y) {
    return builder.prod(x, y);
  }

  @Override
  public <N extends Number> Expression<N> diff(Expression<? extends N> x, Expression<? extends N> y) {
    return builder.diff(x, y);
  }

  @Override
  public <N extends Number> Expression<N> diff(Expression<? extends N> x, N y) {
    return builder.diff(x, y);
  }

  @Override
  public <N extends Number> Expression<N> diff(N x, Expression<? extends N> y) {
    return builder.diff(x, y);
  }

  @Override
  public Expression<Number> quot(Expression<? extends Number> x, Expression<? extends Number> y) {
    return builder.quot(x, y);
  }

  @Override
  public Expression<Number> quot(Expression<? extends Number> x, Number y) {
    return builder.quot(x, y);
  }

  @Override
  public Expression<Number> quot(Number x, Expression<? extends Number> y) {
    return builder.quot(x, y);
  }

  @Override
  public Expression<Integer> mod(Expression<Integer> x, Expression<Integer> y) {
    return builder.mod(x, y);
  }

  @Override
  public Expression<Integer> mod(Expression<Integer> x, Integer y) {
    return builder.mod(x, y);
  }

  @Override
  public Expression<Integer> mod(Integer x, Expression<Integer> y) {
    return builder.mod(x, y);
  }

  @Override
  public Expression<Double> sqrt(Expression<? extends Number> x) {
    return builder.sqrt(x);
  }

  @Override
  public Expression<Long> toLong(Expression<? extends Number> number) {
    return builder.toLong(number);
  }

  @Override
  public Expression<Integer> toInteger(Expression<? extends Number> number) {
    return builder.toInteger(number);
  }

  @Override
  public Expression<Float> toFloat(Expression<? extends Number> number) {
    return builder.toFloat(number);
  }

  @Override
  public Expression<Double> toDouble(Expression<? extends Number> number) {
    return builder.toDouble(number);
  }

  @Override
  public Expression<BigDecimal> toBigDecimal(Expression<? extends Number> number) {
    return builder.toBigDecimal(number);
  }

  @Override
  public Expression<BigInteger> toBigInteger(Expression<? extends Number> number) {
    return builder.toBigInteger(number);
  }

  @Override
  public Expression<String> toString(Expression<Character> character) {
    return builder.toString(character);
  }

  @Override
  public <T> Expression<T> literal(T value) {
    return builder.literal(value);
  }

  @Override
  public <T> Expression<T> nullLiteral(Class<T> resultClass) {
    return builder.nullLiteral(resultClass);
  }

  @Override
  public <T> ParameterExpression<T> parameter(Class<T> paramClass) {
    return builder.parameter(paramClass);
  }

  @Override
  public <T> ParameterExpression<T> parameter(Class<T> paramClass, String name) {
    return builder.parameter(paramClass, name);
  }

  @Override
  public <C extends Collection<?>> Predicate isEmpty(Expression<C> collection) {
    return builder.isEmpty(collection);
  }

  @Override
  public <C extends Collection<?>> Predicate isNotEmpty(Expression<C> collection) {
    return builder.isNotEmpty(collection);
  }

  @Override
  public <C extends Collection<?>> Expression<Integer> size(Expression<C> collection) {
    return builder.size(collection);
  }

  @Override
  public <C extends Collection<?>> Expression<Integer> size(C collection) {
    return builder.size(collection);
  }

  @Override
  public <E, C extends Collection<E>> Predicate isMember(Expression<E> elem, Expression<C> collection) {
    return builder.isMember(elem, collection);
  }

  @Override
  public <E, C extends Collection<E>> Predicate isMember(E elem, Expression<C> collection) {
    return builder.isMember(elem, collection);
  }

  @Override
  public <E, C extends Collection<E>> Predicate isNotMember(Expression<E> elem, Expression<C> collection) {
    return builder.isNotMember(elem, collection);
  }

  @Override
  public <E, C extends Collection<E>> Predicate isNotMember(E elem, Expression<C> collection) {
    return builder.isNotMember(elem, collection);
  }

  @Override
  public <V, M extends Map<?, V>> Expression<Collection<V>> values(M map) {
    return builder.values(map);
  }

  @Override
  public <K, M extends Map<K, ?>> Expression<Set<K>> keys(M map) {
    return builder.keys(map);
  }

  @Override
  public Predicate like(Expression<String> x, Expression<String> pattern) {
    return builder.like(x, pattern);
  }

  @Override
  public Predicate like(Expression<String> x, String pattern) {
    return builder.like(x, pattern);
  }

  @Override
  public Predicate like(Expression<String> x, Expression<String> pattern, Expression<Character> escapeChar) {
    return builder.like(x, pattern, escapeChar);
  }

  @Override
  public Predicate like(Expression<String> x, Expression<String> pattern, char escapeChar) {
    return builder.like(x, pattern, escapeChar);
  }

  @Override
  public Predicate like(Expression<String> x, String pattern, Expression<Character> escapeChar) {
    return builder.like(x, pattern, escapeChar);
  }

  @Override
  public Predicate like(Expression<String> x, String pattern, char escapeChar) {
    return builder.like(x, pattern, escapeChar);
  }

  public Predicate ilike(Expression<String> x, Expression<String> pattern) {
    return builder.like(lower(x), lower(pattern));
  }

  public Predicate ilike(Expression<String> x, String pattern) {
    return builder.like(lower(x), pattern.toLowerCase());
  }

  public Expression<Boolean> contains(Expression<String> field, String value) {
    return new ContainsFunction((CriteriaBuilderImpl) builder, field, value);
  }

  public Predicate ilike(Expression<String> x, Expression<String> pattern, Expression<Character> escapeChar) {
    return builder.like(lower(x), lower(pattern), escapeChar);
  }

  public Predicate ilike(Expression<String> x, Expression<String> pattern, char escapeChar) {
    return builder.like(lower(x), lower(pattern), escapeChar);
  }

  public Predicate ilike(Expression<String> x, String pattern, Expression<Character> escapeChar) {
    return builder.like(lower(x), pattern.toLowerCase(), escapeChar);
  }

  public Predicate ilike(Expression<String> x, String pattern, char escapeChar) {
    return builder.like(lower(x), pattern, escapeChar);
  }

  @Override
  public Predicate notLike(Expression<String> x, Expression<String> pattern) {
    return builder.notLike(x, pattern);
  }

  @Override
  public Predicate notLike(Expression<String> x, String pattern) {
    return builder.notLike(x, pattern);
  }

  @Override
  public Predicate notLike(Expression<String> x, Expression<String> pattern, Expression<Character> escapeChar) {
    return builder.notLike(x, pattern, escapeChar);
  }

  @Override
  public Predicate notLike(Expression<String> x, Expression<String> pattern, char escapeChar) {
    return builder.notLike(x, pattern, escapeChar);
  }

  @Override
  public Predicate notLike(Expression<String> x, String pattern, Expression<Character> escapeChar) {
    return builder.notLike(x, pattern, escapeChar);
  }

  @Override
  public Predicate notLike(Expression<String> x, String pattern, char escapeChar) {
    return builder.notLike(x, pattern, escapeChar);
  }


  public Predicate notIlike(Expression<String> x, Expression<String> pattern) {
    return builder.notLike(lower(x), lower(pattern));
  }


  public Predicate notIlike(Expression<String> x, String pattern) {
    return builder.notLike(lower(x), pattern.toLowerCase());
  }


  public Predicate notIlike(Expression<String> x, Expression<String> pattern, Expression<Character> escapeChar) {
    return builder.notLike(lower(x), lower(pattern), escapeChar);
  }


  public Predicate notIlike(Expression<String> x, Expression<String> pattern, char escapeChar) {
    return builder.notLike(lower(x), lower(pattern), escapeChar);
  }


  public Predicate notIlike(Expression<String> x, String pattern, Expression<Character> escapeChar) {
    return builder.notLike(lower(x), pattern.toLowerCase(), escapeChar);
  }


  public Predicate notIlike(Expression<String> x, String pattern, char escapeChar) {
    return builder.notLike(lower(x), pattern.toLowerCase(), escapeChar);
  }

  @Override
  public Expression<String> concat(Expression<String> x, Expression<String> y) {
    return builder.concat(x, y);
  }

  @Override
  public Expression<String> concat(Expression<String> x, String y) {
    return builder.concat(x, y);
  }

  @Override
  public Expression<String> concat(String x, Expression<String> y) {
    return builder.concat(x, y);
  }

  @Override
  public Expression<String> substring(Expression<String> x, Expression<Integer> from) {
    return builder.substring(x, from);
  }

  @Override
  public Expression<String> substring(Expression<String> x, int from) {
    return builder.substring(x, from);
  }

  @Override
  public Expression<String> substring(Expression<String> x, Expression<Integer> from, Expression<Integer> len) {
    return builder.substring(x, from, len);
  }

  @Override
  public Expression<String> substring(Expression<String> x, int from, int len) {
    return builder.substring(x, from, len);
  }

  @Override
  public Expression<String> trim(Expression<String> x) {
    return builder.trim(x);
  }

  @Override
  public Expression<String> trim(Trimspec ts, Expression<String> x) {
    return builder.trim(ts, x);
  }

  @Override
  public Expression<String> trim(Expression<Character> t, Expression<String> x) {
    return builder.trim(t, x);
  }

  @Override
  public Expression<String> trim(Trimspec ts, Expression<Character> t, Expression<String> x) {
    return builder.trim(ts, t, x);
  }

  @Override
  public Expression<String> trim(char t, Expression<String> x) {
    return builder.trim(t, x);
  }

  @Override
  public Expression<String> trim(Trimspec ts, char t, Expression<String> x) {
    return builder.trim(ts, t, x);
  }

  @Override
  public Expression<String> lower(Expression<String> x) {
    return builder.lower(x);
  }

  @Override
  public Expression<String> upper(Expression<String> x) {
    return builder.upper(x);
  }

  @Override
  public Expression<Integer> length(Expression<String> x) {
    return builder.length(x);
  }

  @Override
  public Expression<Integer> locate(Expression<String> x, Expression<String> pattern) {
    return builder.locate(x, pattern);
  }

  @Override
  public Expression<Integer> locate(Expression<String> x, String pattern) {
    return builder.locate(x, pattern);
  }

  @Override
  public Expression<Integer> locate(Expression<String> x, Expression<String> pattern, Expression<Integer> from) {
    return builder.locate(x, pattern, from);
  }

  @Override
  public Expression<Integer> locate(Expression<String> x, String pattern, int from) {
    return builder.locate(x, pattern, from);
  }

  @Override
  public Expression<Date> currentDate() {
    return builder.currentDate();
  }

  @Override
  public Expression<Timestamp> currentTimestamp() {
    return builder.currentTimestamp();
  }

  @Override
  public Expression<Time> currentTime() {
    return builder.currentTime();
  }

  @Override
  public <T> In<T> in(Expression<? extends T> expression) {
    return builder.in(expression);
  }

  @Override
  public <Y> Expression<Y> coalesce(Expression<? extends Y> x, Expression<? extends Y> y) {
    return builder.coalesce(x, y);
  }

  @Override
  public <Y> Expression<Y> coalesce(Expression<? extends Y> x, Y y) {
    return builder.coalesce(x, y);
  }

  @Override
  public <Y> Expression<Y> nullif(Expression<Y> x, Expression<?> y) {
    return builder.nullif(x, y);
  }

  @Override
  public <Y> Expression<Y> nullif(Expression<Y> x, Y y) {
    return builder.nullif(x, y);
  }

  @Override
  public <T> Coalesce<T> coalesce() {
    return builder.coalesce();
  }

  @Override
  public <C, R> SimpleCase<C, R> selectCase(Expression<? extends C> expression) {
    return builder.selectCase(expression);
  }

  @Override
  public <R> Case<R> selectCase() {
    return builder.selectCase();
  }

  @Override
  public <T> Expression<T> function(String name, Class<T> type, Expression<?>... args) {
    return builder.function(name, type, args);
  }

  public Path<?> getParentPath() {
    return root.getParentPath();
  }

  public <Y> Path<Y> get(SingularAttribute<? super T, Y> attribute) {
    return root.get(attribute);
  }

  public <E, C extends Collection<E>> Expression<C> get(PluralAttribute<T, C, E> collection) {
    return root.get(collection);
  }

  public <K, V, M extends Map<K, V>> Expression<M> get(MapAttribute<T, K, V> map) {
    return root.get(map);
  }

  public <Y> Path<Y> get(String attributeName) {
    return root.get(attributeName);
  }

  public CriteriaQuery distinct(boolean distinct) {
    return (query = query.distinct(distinct));
  }

  public CriteriaQuery orderBy(List<Order> o) {
    return (query = query.orderBy(o));
  }

  public CriteriaQuery orderBy(Order... o) {
    return (query = query.orderBy(o));
  }

  public CriteriaQuery having(Predicate... restrictions) {
    return (query = query.having(restrictions));
  }

  public CriteriaQuery having(Expression<Boolean> restriction) {
    return (query = query.having(restriction));
  }

  public CriteriaQuery groupBy(List<Expression<?>> grouping) {
    return (query = query.groupBy(grouping));
  }

  public CriteriaQuery groupBy(Expression<?>... grouping) {
    return (query = query.groupBy(grouping));
  }

  public CriteriaQuery where(Predicate... restrictions) {
    return (query = query.where(restrictions));
  }

  public CriteriaQuery where(Expression<Boolean> restriction) {
    return (query = query.where(restriction));
  }

  public CriteriaQuery select(Selection selection) {
    return (query = query.select(selection));
  }
}
