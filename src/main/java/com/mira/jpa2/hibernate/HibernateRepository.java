package com.mira.jpa2.hibernate;

import com.mira.jpa2.*;
import org.hibernate.jpa.HibernateEntityManager;

import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Реализация репозитория на основе Hibernate
 */
public class HibernateRepository implements Repository {

    @PersistenceContext
    protected HibernateEntityManager entityManager;

    @Override
    public <T> List<T> findAll(Class<T> cl) {
        CriteriaQuery<T> query = createQuery(cl);
        return find(query);
    }

    /**
     * Создаёт запрос на извлечение базового класса
     *
     * @param cl класс
     * @return запрос с заполненным from
     */
    private <T> CriteriaQuery<T> createQuery(Class<T> cl) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(cl);
        query.from(cl);
        return query;
    }

    @Override
    public <T> List<T> find(Class<T> cl, QueryBuilder<T> queryBuilder) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(cl);
        Root<T> root = query.from(cl);
        queryBuilder.build(query, root, getCriteriaBuilder());
        return find(query);
    }

    @Override
    public <T> T find(Class<T> cl, Object id) {
        return id != null ? entityManager.find(cl, id) : null;
    }

    @Override
    public <T> List<T> find(CriteriaQuery<T> query) {
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public <T> List<T> findAnd(Class<T> cl, final Parameters<T> params) {
        return find(cl, getWhereBuilder(params));
    }

    /**
     * Создаёт построитель условия на основе карты параметров
     *
     * @param params карта параметров
     * @param <T>    класс сущности
     * @return построитель условия
     */
    protected <T> QueryBuilder<T> getWhereBuilder(final Parameters<T> params) {
        return new QueryBuilder<T>() {
            @Override
            protected void build() {
                List<Predicate> predicates = new ArrayList<>(params.size());
                for (SingularAttribute<T, ?> attribute : params.keySet()) {
                    Object value = params.get(attribute);
                    Path<?> path = get(attribute);
                    Predicate predicate = value != null
                            ? equal(path, value)
                            : isNull(path);
                    predicates.add(predicate);
                }
                where(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @Override
    public void saveAll(Iterable entities) {
        for (Object entity : entities) {
            save(entity);
        }
    }

    @Override
    public <T> T save(T entity) {
        entityManager.getSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void delete(Object entity) {
        entityManager.remove(entityManager.merge(entity));
    }

    @Override
    public void deleteAll(Iterable entities) {
        for (Object entity : entities) {
            delete(entity);
        }
    }

    @Override
    public <T> long count(Class<T> cl) {
        return count(cl, new Parameters<T>());
    }

    @Override
    public <T> long count(Class<T> cl, QueryBuilder<T> queryBuilder) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<T> root = query.from(cl);
        query.select(builder.count(root));
        queryBuilder.build(query, root, builder);
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public <T> long count(Class<T> cl, Parameters<T> params) {
        return count(cl, getWhereBuilder(params));
    }

    @Override
    public <T> PageResponse<T> findAll(Class<T> cl, PageRequest<T> pageRequest) {
        return findAnd(cl, pageRequest, new Parameters<T>());
    }

    @Override
    public <T> PageResponse<T> find(Class<T> cl, PageRequest<T> pageRequest, QueryBuilder<T> queryBuilder) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(cl);
        Root<T> root = query.from(cl);
        //условие отбора
        queryBuilder.build(query, root, builder);
        //параметры сортировки
        Orders<T> orders = pageRequest.getOrders();
        List<Order> orderList = new ArrayList<>();
        for (Map.Entry<SingularAttribute<T, ?>, Boolean> entry : orders) {
            SingularAttribute<T, ?> key = entry.getKey();
            Boolean direction = entry.getValue();
            orderList.add(direction
                    ? builder.asc(root.get(key))
                    : builder.desc(root.get(key)));
        }
        query.orderBy(orderList);

        //запрос на количество элементов
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(cl);
        countQuery.select(builder.count(countRoot));
        queryBuilder.build(countQuery, countRoot, builder);

        return find(query, countQuery, pageRequest);
    }

    /**
     * Выполняет запрос с постраничным извлечением данных
     *
     * @param query       запрос
     * @param pageRequest запрос на постраничное извлечение данных
     * @param <T>         тип извлекаемых данных
     * @return страницу
     */
    protected <T> PageResponse<T> find(CriteriaQuery<T> query, CriteriaQuery<Long> countQuery, PageRequest<T> pageRequest) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageRequest.getStartPosition());
        long pageSize = pageRequest.getPageSize();
        typedQuery.setMaxResults((int) pageSize);
        List<T> result = typedQuery.getResultList();

        long count = entityManager.createQuery(countQuery).getSingleResult();
        long pageCount = (count != 0) ? (long) Math.ceil(count / (float) pageSize) : 0;


        return new PageResponse<T>(result, pageRequest.getPage(), pageCount, count);
    }


    @Override
    public <T> PageResponse<T> findAnd(Class<T> cl, PageRequest<T> pageRequest, Parameters<T> params) {
        return find(cl, pageRequest, getWhereBuilder(params));
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return entityManager.createQuery(criteriaQuery);
    }
}
