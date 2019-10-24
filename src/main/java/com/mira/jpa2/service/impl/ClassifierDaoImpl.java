package com.mira.jpa2.service.impl;

import com.mira.jpa2.*;
import com.mira.jpa2.data.Classifier;
import com.mira.jpa2.data.Classifier_;
import com.mira.jpa2.service.ClassifierDao;
import com.mira.utils.ClassUtils;

/**
 * Родительский сервис для работы с классификаторами
 */
public abstract class ClassifierDaoImpl<T extends Classifier>
    extends DictionaryDaoImpl<T>
    implements ClassifierDao<T> {
  private boolean useCacheForFindByCode;

  public ClassifierDaoImpl(Repository repository) {
    super(repository);
  }

  public boolean isUseCacheForFindByCode() {
    return useCacheForFindByCode;
  }

  public void setUseCacheForFindByCode(boolean useCacheForFindByCode) {
    this.useCacheForFindByCode = useCacheForFindByCode;
  }

  @Override
  public T findByCode(String code) {
    return findAndSingle(new Parameters<T>(Classifier_.code, code).setCache(useCacheForFindByCode));
  }

  @Override
  public PageResponse<T> search(String code, String name, PageRequest<T> request) {
    return find(new QueryBuilder<T>() {
      @Override
      protected void build() {
        where(and(ilike(get(Classifier_.code), (code != null ? code : "") + "%")
            , ilike(get(Classifier_.name), (name != null ? name : "") + "%")));
      }
    }, request);
  }

  @Override
  public T findOrCreate(String code, String name) {
    T result = findByCode(code);
    if (result == null) {
      result = ClassUtils.newInstance(getEntityClass());
      result.setCode(code);
      result.setName(name);
      save(result);
    }
    return result;
  }
}
