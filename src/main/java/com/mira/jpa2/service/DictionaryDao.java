package com.mira.jpa2.service;

import com.mira.jpa2.data.DictionaryObject;

import java.util.List;

public interface DictionaryDao<E extends DictionaryObject> extends DefaultDao<E> {
  /**
   * Находит все элементы словаря с указанным именем
   *
   * @param name имя
   * @return список элементов
   */
  List<E> findByName(String name);

  /**
   * Находит список элекментов словаря по имени, если ни одного элемента на найдено, то создаёт новый
   *
   * @param name имя
   * @return элементы словаря
   */
  List<E> findOrCreate(String name);
}
