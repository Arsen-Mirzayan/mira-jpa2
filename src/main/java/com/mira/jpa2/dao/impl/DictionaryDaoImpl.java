package com.mira.jpa2.dao.impl;

import com.mira.jpa2.Parameters;
import com.mira.jpa2.Repository;
import com.mira.jpa2.data.DictionaryObject;
import com.mira.jpa2.data.DictionaryObject_;
import com.mira.jpa2.dao.DictionaryDao;
import com.mira.utils.ClassUtils;
import com.mira.utils.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Сервис для работы со словарями
 */
public abstract class DictionaryDaoImpl<T extends DictionaryObject>
    extends DefaultDaoImpl<T>
    implements DictionaryDao<T> {

  public DictionaryDaoImpl(Repository repository) {
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
