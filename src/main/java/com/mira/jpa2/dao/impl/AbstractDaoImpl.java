package com.mira.jpa2.dao.impl;

import com.mira.jpa2.*;
import com.mira.jpa2.data.AbstractPersistentObject;
import com.mira.jpa2.dao.AbstractDao;
import com.mira.utils.collections.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Родительский класс для всех сервисов по работе с объектам данных. Содержит основные методы для манипуляции данными
 */
public abstract class AbstractDaoImpl<E extends AbstractPersistentObject<IdClass>, IdClass> implements AbstractDao<E, IdClass> {

  protected Repository repository;

  public AbstractDaoImpl(Repository repository) {
    this.repository = repository;
  }

  /**
   * Обрабатывает сущность перед тем, как представить её во внешний мир. Например инициализирует нужные свойства.
   *
   * @param entity объект, который нужно обработать. <b>Может быть null</b>
   * @return переданный объект после инициализации
   */
  protected E process(E entity) {
    return entity;
  }

  @Override
  public E findById(IdClass id) {
    return process(getRepository().find(getEntityClass(), id));
  }

  @Override
  public void save(E entity) {
    save(Collections.singletonList(entity));
  }

  @Override
  public void save(Iterable<E> entities) {
    getRepository().saveAll(entities);
  }

  @Override
  public List<E> findAll() {
    List<E> result = getRepository().findAll(getEntityClass());
    process(result);
    return result;
  }

  @Override
  public PageResponse<E> findAll(PageRequest<E> pageRequest) {
    PageResponse<E> result = getRepository().findAll(getEntityClass(), pageRequest);
    process(result.getResult());
    return result;
  }

  /**
   * Обрабатывает сущности перед тем, как представить её во внешний мир. Например инициализирует нужные свойства.
   *
   * @param entities объект, который нужно обработать. <b>Может быть null</b>
   * @param <T> коллекция объектов
   * @return переданную коллекцию
   */
  protected <T extends Iterable<E>> T process(T entities) {
    for (E entity : entities) {
      process(entity);
    }
    return entities;
  }

  @Override
  public void delete(E... deleted) {
    delete(Arrays.asList(deleted));
  }

  @Override
  public void delete(Iterable<E> deleted) {
    getRepository().deleteAll(deleted);
  }

  @Override
  public long count(Parameters<E> params) {
    return getRepository().count(getEntityClass(), params);
  }

  @Override
  public long count(QueryBuilder<E> builder) {
    return getRepository().count(getEntityClass(), builder);
  }

  @Override
  public List<E> findAnd(Parameters<E> params) {
    return process(getRepository().findAnd(getEntityClass(), params));
  }

  @Override
  public PageResponse<E> findAnd(Parameters<E> params, PageRequest<E> pageRequest) {
    PageResponse<E> response = getRepository().findAnd(getEntityClass(), pageRequest, params);
    process(response.getResult());
    return response;

  }

  @Override
  public boolean exists(Parameters<E> parameters) {
    PageRequest<E> pageRequest = new PageRequest<>(0, 1, new Orders<>());
    return getRepository().findAnd(getEntityClass(), pageRequest, parameters).getRecordCount() != 0;
  }


  @Override
  public PageResponse<E> findPage(int page, int pageSize) {
    PageResponse<E> response =
        getRepository().findAnd(getEntityClass(),
            new PageRequest<>(page, pageSize, new Orders<E>()),
            new Parameters<E>());
    process(response.getResult());
    return response;

  }

  @Override
  public List<E> find(QueryBuilder<E> builder) {
    return find(builder, (Integer) null);
  }

  @Override
  public List<E> find(QueryBuilder<E> builder, Integer maxResult) {
    return process(getRepository().find(getEntityClass(), builder, maxResult));
  }

  @Override
  public PageResponse<E> find(QueryBuilder<E> builder, PageRequest<E> pageRequest) {
    PageResponse<E> response = getRepository().find(getEntityClass(), pageRequest, builder);
    process(response.getResult());
    return response;
  }

  @Override
  public E findAndSingle(Parameters<E> params) {
    return process(CollectionUtils.getSingleElement(findAnd(params)));
  }

  @Override
  public Repository getRepository() {
    return repository;
  }
}
