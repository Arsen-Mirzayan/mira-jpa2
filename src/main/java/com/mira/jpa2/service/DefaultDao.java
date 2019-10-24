package com.mira.jpa2.service;

import com.mira.jpa2.Parameters;
import com.mira.jpa2.QueryBuilder;
import com.mira.jpa2.data.DefaultPersistentObject;

import java.util.List;

public interface DefaultDao<E extends DefaultPersistentObject> extends AbstractDao<E, Long> {

  /**
   * Ищет произвольную страницу подходящую под указанные параметры
   *
   * @param count количество записей на странице
   * @return произвольная страница с результатами
   */
  List<E> findRandom(long count);

  /**
   * Возвращает список из произвольных элементов
   *
   * @param parameters параметры запроса
   * @param count      количество
   * @return список произвольных элементов
   */
  List<E> findRandom(Parameters<E> parameters, long count);

  /**
   * @return итератор, проходящий по всем элементам списка
   */
  Iterable<E> iterate();

  /**
   * Создаёт итератор, который проходит по полному результату запроса. Данные из базы выгружаются постранично.
   *
   * @param queryBuilder запрос
   * @return итератор
   */
  Iterable<E> iterate(QueryBuilder<E> queryBuilder);
}
