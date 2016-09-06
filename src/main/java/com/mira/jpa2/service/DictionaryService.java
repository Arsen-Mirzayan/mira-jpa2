package com.mira.jpa2.service;

import com.mira.jpa2.Parameters;
import com.mira.jpa2.data.DictionaryObject;
import com.mira.jpa2.data.DictionaryObject_;
import com.mira.utils.ClassUtils;
import com.mira.utils.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Сервис для работы со словарями
 */
public abstract class DictionaryService<T extends DictionaryObject> extends DefaultDalService<T> {
  /**
   * Находит все элементы словаря с указанным именем
   *
   * @param name имя
   * @return список элементов
   */
  public List<T> findByName(String name) {
    return findAnd(new Parameters<T>(DictionaryObject_.name, name));
  }

  /**
   * Находит список элекментов словаря по имени, если ни одного элемента на найдено, то создаёт новый
   *
   * @param name имя
   * @return элементы словаря
   */
  public List<T> findOrCreate(String name) {
    List<T> result = findByName(name);
    if (CollectionUtils.isNotEmpty(result)) {
      return result;
    }

    T entity = ClassUtils.newInstance(getEntityClass());
    entity.setName(name);
    save(entity);
    return Collections.singletonList(entity);
  }
}
