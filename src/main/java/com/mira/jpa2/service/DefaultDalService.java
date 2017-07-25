package com.mira.jpa2.service;

import com.mira.jpa2.*;
import com.mira.jpa2.data.DefaultPersistentObject;
import com.mira.jpa2.data.DefaultPersistentObject_;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Родительский сервис для всех сервисов для данных с суррогатным ключом
 */
public abstract class DefaultDalService<E extends DefaultPersistentObject> extends AbstractService<E, Long> {

  /**
   * Ищет произвольную страницу подходящую под указанные параметры
   *
   * @param count количество записей на странице
   * @return произвольная страница с результатами
   */
  public List<E> findRandom(long count) {
    List<E> result = new LinkedList<>();
    long total = repository.count(getEntityClass());
    while (total > 0 && result.size() < count) {
      long startPosition = (long) Math.floor(total * Math.random());
      result.add(repository.find(getEntityClass(), new QueryBuilder<E>() {
        @Override
        protected void build() {
          Predicate where = null;
          for (E object : result) {
            where = and(where, not(equal(get(DefaultPersistentObject_.id), object.getId())));
          }
          if (where != null) {
            where(where);
          }
        }
      }, startPosition, 1).get(0));
      total--;
    }
    return result;
  }

  /**
   * Возвращает список из произвольных элементов
   *
   * @param parameters параметры запроса
   * @param count      количество
   * @return список произвольных элементов
   */
  public List<E> findRandom(Parameters<E> parameters, long count) {
    long total = repository.count(getEntityClass(), parameters);
    long startPosition = total <= count ? 0 : (long) Math.floor((total - count) * Math.random());
    return repository.findAnd(getEntityClass(), parameters, startPosition, (int) count);
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
