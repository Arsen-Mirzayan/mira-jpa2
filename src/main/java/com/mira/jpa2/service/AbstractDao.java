package com.mira.jpa2.service;

import com.mira.jpa2.*;
import com.mira.jpa2.data.AbstractPersistentObject;

import java.util.List;

public interface AbstractDao<E extends AbstractPersistentObject<IdClass>, IdClass> {
  Class<E> getEntityClass();

  /**
   * Находит треуемый объект по идентификатору
   *
   * @param id идентификатор
   * @return найденный объект или {@code null}
   */
  E findById(IdClass id);

  /**
   * Сохраняет указанные объекты
   *
   * @param entities список объектов
   */
  void save(E... entities);

  /**
   * Сохраняет указанные объекты
   *
   * @param entities объекты
   */
  void save(Iterable<E> entities);

  /**
   * Находит все объекты данного класса
   *
   * @return список объектов
   */
  List<E> findAll();

  /**
   * Находит все объекты данного класса и возвращает их постранично
   *
   * @param pageRequest запрос
   * @return страница ответа
   */
  PageResponse<E> findAll(PageRequest<E> pageRequest);

  /**
   * Удаляет указанный объект из базы данных
   *
   * @param deleted удаляемый объект
   */
  void delete(E... deleted);

  /**
   * Удаляет указанный объект из базы данных
   *
   * @param deleted удаляемый объект
   */
  void delete(Iterable<E> deleted);

  /**
   * Находит количество экземпляров класса. подходящих под указанные параметры
   *
   * @param params параметры
   * @return количество элементов данного класса
   */
  long count(Parameters<E> params);

  /**
   * Находит количество экземпляров класса. подходящих под указанные параметры
   *
   * @param builder построитель условия
   * @return количество элементов данного класса
   */
  long count(QueryBuilder<E> builder);

  /**
   * Находит список элементов по указанным параметрам. Условия на параметры соединяются условием and
   *
   * @param params параметры
   * @return список найденных объектов
   */
  List<E> findAnd(Parameters<E> params);

  /**
   * Находит список элементов по указанным параметрам. Условия на параметры соединяются условием and.
   *
   * @param params      параметры
   * @param pageRequest параметры страничного запроса
   * @return страница ответа
   */
  PageResponse<E> findAnd(Parameters<E> params, PageRequest<E> pageRequest);

  /**
   * Проверяет, существуют ли элементы, подходящие под указанные параметры
   *
   * @param parameters параметры
   * @return {@code true} если существуют
   */
  boolean exists(Parameters<E> parameters);

  /**
   * Находит список элементов для указанной страницы.
   *
   * @param page     номер страницы, начиная с 0.
   * @param pageSize размер страницы
   * @return страница ответа
   */
  PageResponse<E> findPage(int page, int pageSize);

  /**
   * Находит список объектов
   *
   * @param builder построитель условия
   * @return результат
   */
  List<E> find(QueryBuilder<E> builder);

  /**
   * Находит список объектов
   *
   * @param builder   построитель условия
   * @param maxResult максимальное количество результирующих строк
   * @return результат
   */
  List<E> find(QueryBuilder<E> builder, Integer maxResult);

  /**
   * Находит список объектов для постраничного поиска
   *
   * @param builder     построитель условия
   * @param pageRequest параметры страничного запроса
   * @return страница ответа
   */
  PageResponse<E> find(QueryBuilder<E> builder, PageRequest<E> pageRequest);

  /**
   * Возвращает единственный элемент, найденный по указанным параметрам. Если под указанные параметры не подходит, то
   * возвращается {@code null}, если же более одного элемента, то {@link IllegalArgumentException}
   *
   * @param params параметры
   * @return найденный элемент или {@code null} если ничего не найдено.
   * @throws IllegalArgumentException если найдено более одного элемента
   */
  E findAndSingle(Parameters<E> params);

  Repository getRepository();
}
