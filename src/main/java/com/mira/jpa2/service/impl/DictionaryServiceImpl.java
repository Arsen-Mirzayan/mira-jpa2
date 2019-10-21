package com.mira.jpa2.service.impl;

import com.mira.jpa2.Parameters;
import com.mira.jpa2.Repository;
import com.mira.jpa2.data.DictionaryObject;
import com.mira.jpa2.data.DictionaryObject_;
import com.mira.jpa2.service.DictionaryService;
import com.mira.utils.ClassUtils;
import com.mira.utils.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Сервис для работы со словарями
 */
public abstract class DictionaryServiceImpl<T extends DictionaryObject>
    extends DefaultDalServiceImpl<T>
    implements DictionaryService<T> {

  public DictionaryServiceImpl(Repository repository) {
    super(repository);
  }

  @Override
  public List<T> findByName(String name) {
    return findAnd(new Parameters<T>(DictionaryObject_.name, name));
  }

  @Override
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
