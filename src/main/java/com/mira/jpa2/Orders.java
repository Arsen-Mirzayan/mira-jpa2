package com.mira.jpa2;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Список параметров сортировки
 *
 * @param <T> класс объектов, список которых сортируем
 */
public class Orders<T> extends LinkedHashMap<SingularAttribute<? super T, ?>, Boolean> implements Iterable<Map.Entry<SingularAttribute<? super T, ?>, Boolean>> {
    /**
     * Создаёт пустой список сортировки
     */
    public Orders() {
    }


    /**
     * Создаёт список сортировки, заполненный указанными свойствами. Все параметры устанавливаются по возрастанию
     */
    public Orders(SingularAttribute<? super T, ?>... properties) {
        asc(properties);
    }


    @Override
    public Iterator<Map.Entry<SingularAttribute<? super T, ?>, Boolean>> iterator() {
        return entrySet().iterator();
    }

    /**
     * Добавляет условия упорядочания по возрастанию по указаным свойствам
     *
     * @param properties свойства
     */
    public void asc(SingularAttribute<? super T, ?>... properties) {
        for (SingularAttribute<? super T, ?> property : properties) {
            put(property, true);
        }
    }

    /**
     * Добавляет условия упорядочания по убыванию по указаным свойствам
     *
     * @param properties свойства
     */
    public void desc(SingularAttribute<? super T, ?>... properties) {
        for (SingularAttribute<? super T, ?> property : properties) {
            put(property, false);
        }
    }


}
