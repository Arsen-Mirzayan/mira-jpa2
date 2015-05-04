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
public class Orders<T> extends LinkedHashMap<SingularAttribute<T, ?>, Boolean> implements Iterable<Map.Entry<SingularAttribute<T, ?>, Boolean>> {
    /**
     * Создаёт пустой список сортировки
     */
    public Orders() {
    }


    /**
     * Создаёт список сортировки, заполненный указанными свойствами. Все параметры устанавливаются по возрастанию
     */
    public Orders(SingularAttribute<T, ?>... properties) {
        asc(properties);
    }


    @Override
    public Iterator<Map.Entry<SingularAttribute<T, ?>, Boolean>> iterator() {
        return entrySet().iterator();
    }

    /**
     * Добавляет условия упорядочания по возрастанию по указаным свойствам
     *
     * @param properties свойства
     */
    public void asc(SingularAttribute<T, ?>... properties) {
        for (SingularAttribute<T, ?> property : properties) {
            put(property, true);
        }
    }

    /**
     * Добавляет условия упорядочания по убыванию по указаным свойствам
     *
     * @param properties свойства
     */
    public void desc(SingularAttribute<T, ?>... properties) {
        for (SingularAttribute<T, ?> property : properties) {
            put(property, false);
        }
    }


}
