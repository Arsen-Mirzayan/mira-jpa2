package com.mira.jpa2.service;

import com.mira.jpa2.PageRequest;
import com.mira.jpa2.PageResponse;
import com.mira.jpa2.Parameters;
import com.mira.jpa2.QueryBuilder;
import com.mira.jpa2.data.Classifier;
import com.mira.jpa2.data.Classifier_;

/**
 * Родительский сервис для работы с классификаторами
 */
public abstract class ClassifierService<T extends Classifier> extends DefaultDalService<T> {
    /**
     * Находит значение классификатора по коду
     *
     * @param code код классификатора
     * @return найденный элемент или {@code null}
     */
    public T findByCode(String code) {
        return findAndSingle(new Parameters<T>(Classifier_.code, code));
    }

    /**
     * Ищет список ОКВЭД, подходящие под указанные код и имя.
     *
     * @param code    код
     * @param name    название
     * @param request параметры запроса
     * @return страница ответа
     */
    public PageResponse<T> search(String code, String name, PageRequest<T> request) {

        return find(new QueryBuilder<T>() {
            @Override
            protected void build() {
                where(and(ilike(get(Classifier_.code), (code != null ? code : "") + "%")
                        , ilike(get(Classifier_.name), (name != null ? name : "") + "%")));
            }
        }, request);
    }
}
