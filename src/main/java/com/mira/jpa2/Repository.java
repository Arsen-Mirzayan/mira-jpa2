package com.mira.jpa2;


import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;

/**
 * Репозиторий, выполняющий работу с данными
 */
public interface Repository {
  /**
   * Возвращает список всех объеков указанного класса
   *
   * @param cl  класс требуемых объектов
   * @param <T> тип объекта
   * @return список найденных объектов
   */
  <T> List<T> findAll(Class<T> cl);

  /**
   * Возвращает список объектов указанного типа, подходящих под построенное условие
   *
   * @param cl           класс требуемых объектов
   * @param queryBuilder построитель условия отбора
   * @param <T>          тип объекта
   * @return список найденных объектов
   */
  <T> List<T> find(Class<T> cl, QueryBuilder<T> queryBuilder);

  /**
   * Возвращает список объектов указанного типа, подходящих под построенное условие
   *
   * @param cl           класс требуемых объектов
   * @param queryBuilder построитель условия отбора
   * @param maxResult    максимальное количество результирующих строк
   * @param <T>          тип объекта
   * @return список найденных объектов
   */
  <T> List<T> find(Class<T> cl, QueryBuilder<T> queryBuilder, Integer maxResult);

  /**
   * Возвращает список объектов указанного типа, подходящих под построенное условие
   *
   * @param cl           класс требуемых объектов
   * @param queryBuilder построитель условия отбора
   * @param firstResult  начиная с какой строки ищем
   * @param maxResult    максимальное количество результирующих строк
   * @param <T>          тип объекта
   * @return список найденных объектов
   */
  <T> List<T> find(Class<T> cl, QueryBuilder<T> queryBuilder, long firstResult, Integer maxResult);

  /**
   * Находит элемент по идентификатору
   *
   * @param cl  класс требуемого объекта
   * @param id  идентификаторв
   * @param <T> тип объекта
   * @return найденный объект, если есть, или {@code null}
   */
  <T> T find(Class<T> cl, Object id);

  /**
   * Возвращает список объектов по запросу
   *
   * @param query запрос
   * @param <T>   тип объекта
   * @return список найденных объектов
   */
  <T> List<T> find(CriteriaQuery<T> query);

  /**
   * Возвращает список объектов по запросу
   *
   * @param query       запрос
   * @param firstResult начианя с какой строки ищем
   * @param maxResult   максимальное количество результирующих строк
   * @param hints       подсказки для запроса
   * @param <T>         тип объекта
   * @return список найденных объектов
   */
  <T> List<T> find(CriteriaQuery<T> query, long firstResult, Integer maxResult, Map<String, Object> hints);

  /**
   * Возвращает список объектов, походящих под указанные параметры. Условия соединяются через конъюнкцию
   *
   * @param cl     класс требуемых объектов
   * @param params картра параметров
   * @param <T>    тип объекта
   * @return список найденных объектов
   */
  <T> List<T> findAnd(Class<T> cl, Parameters<T> params);

  /**
   * Возвращает список объектов, походящих под указанные параметры. Условия соединяются через конъюнкцию
   *
   * @param cl          класс требуемых объектов
   * @param params      картра параметров
   * @param firstResult начиная с какой строки ищем
   * @param maxResult   максимальное количество возвращаемых строк
   * @param <T>         тип объекта
   * @return список найденных объектов
   */
  <T> List<T> findAnd(Class<T> cl, Parameters<T> params, long firstResult, Integer maxResult);

  /**
   * Создаёт инструмент для создания запроса
   *
   * @return билдер запросов
   */
  CriteriaBuilder getCriteriaBuilder();

  /**
   * Сохраняет указанные объекты
   *
   * @param entities перечень объектов
   */
  void saveAll(Iterable entities);

  /**
   * Сохраняет указанный объект
   *
   * @param <T>    тип объекта
   * @param entity объект на сохранение
   * @return сохранённый объект
   */
  <T> T save(T entity);

  /**
   * Удаляет указанный объект
   *
   * @param entity объект на удаление
   */
  void delete(Object entity);

  /**
   * Удаляет указанный объект
   *
   * @param entities объект на удаление
   */
  void deleteAll(Iterable entities);

  /**
   * Возвращает количество элементов указанного класса, хранящихся в хранилище
   *
   * @param cl  класс
   * @param <T> тип объекта
   * @return количество
   */
  <T> long count(Class<T> cl);

  /**
   * Возвращает количество элементов указанного класса, хранящихся в хранилище
   *
   * @param cl           класс
   * @param queryBuilder условие отбора
   * @param <T>          тип объекта
   * @return количество
   */
  <T> long count(Class<T> cl, QueryBuilder<T> queryBuilder);

  /**
   * Возвращает количество элементов указанного класса, хранящихся в хранилище
   *
   * @param cl     класс
   * @param params параметры запроса
   * @param <T>    тип объекта
   * @return количество
   */
  <T> long count(Class<T> cl, Parameters<T> params);

  /**
   * Возвращает список всех объеков указанного класса
   *
   * @param cl          класс требуемых объектов
   * @param <T>         тип объекта
   * @param pageRequest запрос на постраничное извлечение данных
   * @return постраничный результат запроса
   */
  <T> PageResponse<T> findAll(Class<T> cl, PageRequest<T> pageRequest);

  /**
   * Возвращает список объектов указанного типа, подходящих под построенное условие
   *
   * @param cl           класс требуемых объектов
   * @param queryBuilder построитель условия отбора
   * @param pageRequest  запрос на постраничное извлечение данных
   * @param <T>          тип объекта
   * @return список найденных объектов
   */
  <T> PageResponse<T> find(Class<T> cl, PageRequest<T> pageRequest, QueryBuilder<T> queryBuilder);

  /**
   * Возвращает список объектов, походящих под указанные параметры. Условия соединяются через конъюнкцию
   *
   * @param cl          класс требуемых объектов
   * @param params      картра параметров
   * @param pageRequest запрос на постраничное извлечение данных
   * @param <T>         тип объекта
   * @return список найденных объектов
   */
  <T> PageResponse<T> findAnd(Class<T> cl, PageRequest<T> pageRequest, Parameters<T> params);


  void flush();

  /**
   * Создаёт запрос по заготовке
   *
   * @param criteriaQuery заготовка под запрос
   * @param <T>           тип объекта
   * @return готовый запрос
   */
  public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);
}

