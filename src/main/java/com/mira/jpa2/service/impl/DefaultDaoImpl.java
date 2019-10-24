package com.mira.jpa2.service.impl;

import com.mira.jpa2.*;
import com.mira.jpa2.data.DefaultPersistentObject;
import com.mira.jpa2.data.DefaultPersistentObject_;
import com.mira.jpa2.service.DefaultDao;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Родительский сервис для всех сервисов для данных с суррогатным ключом
 */
public abstract class DefaultDaoImpl<E extends DefaultPersistentObject> extends AbstractDaoImpl<E, Long> implements DefaultDao<E> {

  public DefaultDaoImpl(Repository repository) {
    super(repository);
  }

  @Override
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

  @Override
  public List<E> findRandom(Parameters<E> parameters, long count) {
    long total = repository.count(getEntityClass(), parameters);
    long startPosition = total <= count ? 0 : (long) Math.floor((total - count) * Math.random());
    return repository.findAnd(getEntityClass(), parameters, startPosition, (int) count);
  }

  @Override
  public Iterable<E> iterate() {
    return iterate(new QueryBuilder<E>() {
      @Override
      protected void build() {

      }
    });
  }

  @Override
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

    private QueryResultIterator(QueryBuilder<E> queryBuilder) {
      this.queryBuilder = queryBuilder;
    }

    @Override
    public boolean hasNext() {
      if (buffer.size() <= index) {
        page++;
        PageResponse<E> response = find(queryBuilder, new PageRequest<E>(page, bufferSize, new Orders<E>(DefaultPersistentObject_.id)));
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
