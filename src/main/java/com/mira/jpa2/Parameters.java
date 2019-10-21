package com.mira.jpa2;

import javax.persistence.metamodel.SingularAttribute;
import java.util.HashMap;

/**
 * Карта параметров для запросов в репозиторий
 *
 * @see Repository
 */
public class Parameters<T> extends HashMap<SingularAttribute<? super T, ?>, Object> {
  private boolean cache;


  /**
   * Создаёт пустую карту параметров
   */
  public Parameters() {
  }

  public boolean isCache() {
    return cache;
  }

  /**
   * Говорит, должен ли быть запрос кешируемый
   *
   * @param cache {@code true}  если нужно кешировать запрос
   * @return себя же для цепного выхова
   */
  public Parameters<T> setCache(boolean cache) {
    this.cache = cache;
    return this;
  }

  /**
   * Создаёт карту параметров и кладёт туда пару (параметр, значение)
   *
   * @param parameter параметр
   * @param value     значение
   * @param <V>       класс значения
   */
  public <V> Parameters(SingularAttribute<? super T, V> parameter, V value) {
    put(parameter, value);
  }
}
