package com.mira.jpa2.dao.impl;


import com.mira.jpa2.*;
import com.mira.jpa2.data.TreeClassifier;
import com.mira.jpa2.data.TreeClassifier_;
import com.mira.jpa2.dao.TreeClassifierDao;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Родительский сервис для работы с классификаторами
 */
public abstract class TreeClassifierDaoImpl<T extends TreeClassifier<T>>
    extends ClassifierDaoImpl<T>
    implements TreeClassifierDao<T> {

  public TreeClassifierDaoImpl(Repository repository) {
    super(repository);
  }

  @Override
  public List<T> findChildren(T parent) {
    if (parent != null) {
      return findAnd(new Parameters<>(TreeClassifier_.parent, parent));
    } else {
      return findRoot();
    }
  }

  @Override
  public Collection<T> findAllChildren(T parent) {
    Set<T> children = new HashSet<>();
    for (T child : findChildren(parent)) {
      children.add(child);
      children.addAll(findAllChildren(child));
    }
    return children;
  }

  @Override
  public void save(Iterable<T> entities) {
    //Проставим всем родителям, что они больше не родители
    Set<T> parents = new HashSet<>();
    entities.forEach(t -> {
      if (t.getParent() != null) {
        parents.add(t.getParent());
      }
    });
    for (T parent : parents) {
      parent.setLeaf(false);
    }
    super.save(parents);
    //Для текущих сущностей проверим, есть ли у них дети
    entities.forEach(entity -> entity.setLeaf(entity.getId() == null || count(new Parameters<>(TreeClassifier_.parent, entity)) == 0));

    super.save(entities);
  }

  @Override
  public T find(T parent, String name) {
    Parameters<T> parameters = new Parameters<>();
    parameters.put(TreeClassifier_.name, name);
    if (parent != null) {
      parameters.put(TreeClassifier_.parent, parent);
    }
    return findAndSingle(parameters);
  }

  @Override
  public List<T> findLeaf() {
    return findAnd(new Parameters<>(TreeClassifier_.leaf, true));
  }

  @Override
  public List<T> findRoot() {
    return find(new QueryBuilder<T>() {
      @Override
      protected void build() {
        where(isNull(get(TreeClassifier_.parent)));
      }
    });
  }

  @Override
  public PageResponse<T> findRoot(PageRequest<T> pageRequest) {
    return find(new QueryBuilder<T>() {
      @Override
      protected void build() {
        where(isNull(get(TreeClassifier_.parent)));
      }
    }, pageRequest);
  }

  @Override
  public PageResponse<T> findChildren(T parent, PageRequest<T> pageRequest) {
    return findAnd(new Parameters<>(TreeClassifier_.parent, parent), pageRequest);
  }
}
