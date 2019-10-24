package com.mira.jpa2.service;

import com.mira.jpa2.PageRequest;
import com.mira.jpa2.PageResponse;
import com.mira.jpa2.data.Classifier;

public interface ClassifierDao<T extends Classifier> extends DictionaryDao<T> {
  /**
   * Находит значение классификатора по коду
   *
   * @param code код классификатора
   * @return найденный элемент или {@code null}
   */
  T findByCode(String code);

  /**
   * Ищет список, подходящий под указанные код и имя.
   *
   * @param code    код
   * @param name    название
   * @param request параметры запроса
   * @return страница ответа
   */
  PageResponse<T> search(String code, String name, PageRequest<T> request);

  /**
   * Находит элемент классификатора по коду, если не находит,то создаёт новый
   *
   * @param code код
   * @param name имя
   * @return элемент классификатора
   */
  T findOrCreate(String code, String name);
}
