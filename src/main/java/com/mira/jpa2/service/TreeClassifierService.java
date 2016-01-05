package com.mira.jpa2.service;


import com.mira.jpa2.PageRequest;
import com.mira.jpa2.PageResponse;
import com.mira.jpa2.Parameters;
import com.mira.jpa2.QueryBuilder;
import com.mira.jpa2.data.TreeClassifier;
import com.mira.jpa2.data.TreeClassifier_;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Родительский сервис для работы с классификаторами
 */
public abstract class TreeClassifierService<T extends TreeClassifier<T>> extends ClassifierService<T> {

    /**
     * Находит непосредственные дочерние элементы указанного
     *
     * @param parent родительский элемент
     * @return список дочерних элементов
     */
    public List<T> findChildren(T parent) {
        if (parent != null) {
            return findAnd(new Parameters<>(TreeClassifier_.parent, parent));
        } else {
            return findRoot();
        }
    }

    /**
     * Находит все дочерние элементы указанного родителя на всех уровнях вложенности
     *
     * @param parent родительский элемент
     * @return коллекция дочерних элементов
     */
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

    /**
     * Ищет категорию по имени и родителю
     *
     * @param parent родительская категория, может быть пустой.
     * @param name   имя категории
     * @return результат поиска
     */
    public T find(T parent, String name) {
        Parameters<T> parameters = new Parameters<>();
        parameters.put(TreeClassifier_.name, name);
        if (parent != null) {
            parameters.put(TreeClassifier_.parent, parent);
        }
        return findAndSingle(parameters);
    }

    /**
     * Находит все листовые категории из дерева категорий
     *
     * @return список листовых категорий
     */
    public List<T> findLeaf() {
        return findAnd(new Parameters<>(TreeClassifier_.leaf, true));
    }

    /**
     * @return все родительские категории
     */
    public List<T> findRoot() {
        return find(new QueryBuilder<T>() {
            @Override
            protected void build() {
                where(isNull(get(TreeClassifier_.parent)));
            }
        });
    }

    /**
     * Находит список корневых категорий
     *
     * @param pageRequest параметры запроса
     * @return все корневые категории
     */
    public PageResponse<T> findRoot(PageRequest<T> pageRequest) {
        return find(new QueryBuilder<T>() {
            @Override
            protected void build() {
                where(isNull(get(TreeClassifier_.parent)));
            }
        }, pageRequest);
    }

    /**
     * Находит всех прямых потомков указанной категории
     *
     * @param parent      родительская категория
     * @param pageRequest параметры запроса
     * @return список детей
     */
    public PageResponse<T> findChildren(T parent, PageRequest<T> pageRequest) {
        return findAnd(new Parameters<>(TreeClassifier_.parent, parent), pageRequest);
    }
}
