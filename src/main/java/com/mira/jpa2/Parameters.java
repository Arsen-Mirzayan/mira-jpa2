package com.mira.jpa2;

import javax.persistence.metamodel.SingularAttribute;
import java.util.HashMap;

/**
 * Карта параметров для запросов в репозиторий
 *
 * @see Repository
 */
public class Parameters<T> extends HashMap<SingularAttribute<? super T, ?>, Object> {
    /**
     * Создаёт пустую карту параметров
     */
    public Parameters() {
    }

    /**
     * Создаёт карту параметров и кладёт туда пару (параметр, значение)
     *
     * @param parameter параметр
     * @param value     значение
     */
    public <V> Parameters(SingularAttribute<? super T, V> parameter, V value) {
        put(parameter, value);
    }
}
