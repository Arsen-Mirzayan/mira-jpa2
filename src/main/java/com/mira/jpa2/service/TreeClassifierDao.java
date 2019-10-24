package com.mira.jpa2.service;

import com.mira.jpa2.PageRequest;
import com.mira.jpa2.PageResponse;
import com.mira.jpa2.data.TreeClassifier;

import java.util.Collection;
import java.util.List;

public interface TreeClassifierDao<T extends TreeClassifier<T>> extends ClassifierDao<T> {
  /**
   * Находит непосредственные дочерние элементы указанного
   *
   * @param parent родительский элемент
   * @return список дочерних элементов
   */
  List<T> findChildren(T parent);

  /**
   * Находит все дочерние элементы указанного родителя на всех уровнях вложенности
   *
   * @param parent родительский элемент
   * @return коллекция дочерних элементов
   */
  Collection<T> findAllChildren(T parent);

  /**
   * Ищет категорию по имени и родителю
   *
   * @param parent родительская категория, может быть пустой.
   * @param name   имя категории
   * @return результат поиска
   */
  T find(T parent, String name);

  /**
   * Находит все листовые категории из дерева категорий
   *
   * @return список листовых категорий
   */
  List<T> findLeaf();

  /**
   * @return все родительские категории
   */
  List<T> findRoot();

  /**
   * Находит список корневых категорий
   *
   * @param pageRequest параметры запроса
   * @return все корневые категории
   */
  PageResponse<T> findRoot(PageRequest<T> pageRequest);

  /**
   * Находит всех прямых потомков указанной категории
   *
   * @param parent      родительская категория
   * @param pageRequest параметры запроса
   * @return список детей
   */
  PageResponse<T> findChildren(T parent, PageRequest<T> pageRequest);
}
