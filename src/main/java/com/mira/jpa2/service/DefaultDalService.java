package com.mira.jpa2.service;

import com.mira.jpa2.*;
import com.mira.jpa2.data.DefaultPersistentObject;
import com.mira.jpa2.data.DefaultPersistentObject_;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Родительский сервис для всех сервисов для данных с суррогатным ключом
 */
public abstract class DefaultDalService<E extends DefaultPersistentObject> extends AbstractService<E, Long> {

    /**
     * Ищет произвольную страницу подходящую под указанные параметры
     *
     * @param pageSize количество записей на странице
     * @return произвольная страница с результатами
     */
    public PageResponse<E> findRandom(long pageSize) {
        return findRandom(new Parameters<>(), pageSize);
    }

    /**
     * Ищет произвольную страницу подходящую под указанные параметры
     *
     * @param parameters параметры запроса
     * @param pageSize   количество записей на странице
     * @return произвольная страница с результатами
     */
    public PageResponse<E> findRandom(Parameters<E> parameters, long pageSize) {
        long count = repository.count(getEntityClass(), parameters);
        long pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        long page = (long) Math.floor(Math.random() * pageCount);
        Orders<E> orders = new Orders<>(DefaultPersistentObject_.id);
        return findAnd(parameters, new PageRequest<>(page, pageSize, orders));
    }

    /**
     * @return итератор, проходящий по всем элементам списка
     */
    public Iterable<E> iterate() {
        return iterate(new QueryBuilder<E>() {
            @Override
            protected void build() {

            }
        });
    }

    /**
     * Создаёт итератор, который проходит по полному результату запроса. Данные из базы выгружаются постранично.
     *
     * @param queryBuilder запрос
     * @return итератор
     */
    public Iterable<E> iterate(QueryBuilder<E> queryBuilder) {
        return () -> new QueryResultIterator(queryBuilder);
    }

    /**
     * Итератор, ходящий по результатам запроса
     */
    private class QueryResultIterator implements Iterator<E> {
        private List<E> buffer = new ArrayList<>();
        private int index = 0;
        private long page = -1;
        private long bufferSize = 1000;
        private QueryBuilder<E> queryBuilder;

        public QueryResultIterator(QueryBuilder<E> queryBuilder) {
            this.queryBuilder = queryBuilder;
        }

        @Override
        public boolean hasNext() {
            if (buffer.size() <= index) {
                page++;
                PageResponse<E> response = DefaultDalService.this.find(queryBuilder, new PageRequest<E>(page, bufferSize, new Orders<E>(DefaultPersistentObject_.id)));
                buffer = response.getResult();
                index = 0;
            }
            return !buffer.isEmpty();
        }

        @Override
        public E next() {
            return buffer.get(index++);
        }
    }
}
