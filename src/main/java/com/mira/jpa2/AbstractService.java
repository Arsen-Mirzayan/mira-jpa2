package com.mira.jpa2;

import com.mira.utils.collections.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Родительский класс для всех сервисов по работе с объектам данных. Содержит основные методы для манипуляции данными
 */
public abstract class AbstractService<E> {

    protected abstract Class<E> getEntityClass();

    /**
     * Обрабатывает сущность перед тем, как представить её во внешний мир. Например инициализирует нужные свойства.
     *
     * @param entity объект, который нужно обработать. <b>Может быть null</b>
     * @return переданный объект после инициализации
     */
    protected E process(E entity) {
        return entity;
    }

    /**
     * Находит треуемый объект по идентификатору
     *
     * @param id идентификатор
     * @return найденный объект или {@code null}
     */
    public E findById(Long id) {
        return process(getRepository().find(getEntityClass(), id));
    }

    /**
     * Сохраняет указанные объекты
     *
     * @param entities список объектов
     */
    public void save(E... entities) {
        save(Arrays.asList(entities));
    }

    /**
     * Сохраняет указанные объекты
     *
     * @param entities объекты
     */
    public void save(Iterable<E> entities) {
        getRepository().saveAll(entities);
    }

    /**
     * Находит все объекты данного класса
     *
     * @return список объектов
     */
    public List<E> findAll() {
        List<E> result = getRepository().findAll(getEntityClass());
        process(result);
        return result;
    }

    /**
     * Находит все объекты данного класса и возвращает их постранично
     *
     * @param pageRequest запрос
     * @return страница ответа
     */
    public PageResponse<E> findAll(PageRequest<E> pageRequest) {
        PageResponse<E> result = getRepository().findAll(getEntityClass(), pageRequest);
        process(result.getResult());
        return result;
    }

    /**
     * Обрабатывает сущности перед тем, как представить её во внешний мир. Например инициализирует нужные свойства.
     *
     * @param entities объект, который нужно обработать. <b>Может быть null</b>
     * @return переданную коллекцию
     */
    protected <T extends Iterable<E>> T process(T entities) {
        for (E entity : entities) {
            process(entity);
        }
        return entities;
    }

    /**
     * Удаляет указанный объект из базы данных
     *
     * @param deleted удаляемый объект
     */
    public void delete(E... deleted) {
        delete(Arrays.asList(deleted));
    }

    /**
     * Удаляет указанный объект из базы данных
     *
     * @param deleted удаляемый объект
     */
    public void delete(Iterable<E> deleted) {
        getRepository().deleteAll(deleted);
    }

    /**
     * Находит количество экземпляров класса. подходящих под указанные параметры
     *
     * @param params параметры
     * @return количество элементов данного класса
     */
    public long count(Parameters<E> params) {
        return getRepository().count(getEntityClass(), params);
    }

    /**
     * Находит количество экземпляров класса. подходящих под указанные параметры
     *
     * @param builder построитель условия
     * @return количество элементов данного класса
     */
    public long count(QueryBuilder<E> builder) {
        return getRepository().count(getEntityClass(), builder);
    }

    /**
     * Находит список элементов по указанным параметрам. Условия на параметры соединяются условием and
     *
     * @param params параметры
     * @return список найденных объектов
     */
    public List<E> findAnd(Parameters<E> params) {
        return process(getRepository().findAnd(getEntityClass(), params));
    }

    /**
     * Находит список элементов по указанным параметрам. Условия на параметры соединяются условием and.
     *
     * @param params      параметры
     * @param pageRequest параметры страничного запроса
     * @return страница ответа
     */
    public PageResponse<E> findAnd(Parameters<E> params, PageRequest<E> pageRequest) {
        PageResponse<E> response = getRepository().findAnd(getEntityClass(), pageRequest, params);
        process(response.getResult());
        return response;

    }

    /**
     * Находит список элементов для указанной страницы.
     *
     * @return страница ответа
     */
    public PageResponse<E> findPage(int page, int pageSize) {
        PageResponse<E> response =
                getRepository().findAnd(getEntityClass(),
                        new PageRequest<>(page, pageSize, new Orders<E>()),
                        new Parameters<E>());
        process(response.getResult());
        return response;

    }

    /**
     * Находит список объектов
     *
     * @param builder построитель условия
     * @return результат
     */
    public List<E> find(QueryBuilder<E> builder) {
        return find(builder, (Integer) null);
    }

    /**
     * Находит список объектов
     *
     * @param builder   построитель условия
     * @param maxResult максимальное количество результирующих строк
     * @return результат
     */
    public List<E> find(QueryBuilder<E> builder, Integer maxResult) {
        return process(getRepository().find(getEntityClass(), builder, maxResult));
    }

    /**
     * Находит список объектов для постраничного поиска
     *
     * @param builder     построитель условия
     * @param pageRequest параметры страничного запроса
     * @return страница ответа
     */
    public PageResponse<E> find(QueryBuilder<E> builder, PageRequest<E> pageRequest) {
        PageResponse<E> response = getRepository().find(getEntityClass(), pageRequest, builder);
        process(response.getResult());
        return response;
    }

    /**
     * Возвращает единственный элемент, найденный по указанным параметрам. Если под указанные параметры не подходит, то
     * возвращается {@code null}, если же более одного элемента, то {@link IllegalArgumentException}
     *
     * @param params параметры
     * @return найденный элемент или {@code null} если ничего не найдено.
     * @throws IllegalArgumentException если найдено более одного элемента
     */
    public E findAndSingle(Parameters<E> params) {
        return process(CollectionUtils.getSingleElement(findAnd(params)));
    }

    public abstract Repository getRepository();

}
