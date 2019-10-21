package com.mira.jpa2.data;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Абстрактный родитель для хранимых классов
 */
@MappedSuperclass
public abstract class AbstractPersistentObject<IdClass> implements Serializable {
    public AbstractPersistentObject() {
    }

    /**
     * <p>
     * Конструктор с указанием идентификатора. В типичной ситуации объект создаётся и инициализируется контейнером.
     * </p>
     * <p>
     * Типичное использование - создание пустого объекта с идентификатором для поиска в коллекциях.
     * </p>
     *
     * @param id идентификатор
     */
    public AbstractPersistentObject(IdClass id) {
        setId(id);
    }

    /**
     * @return идентификатор объекта
     */
    public abstract IdClass getId();

    /**
     * <p>
     * Устанавливает идентификатор объекта. Не должен использоваться внутри приложения при сохранении новых
     * объектов , идентификаторы новых объектов генерируются автоматически.</p>
     * <p>
     * Типичное использование - создание пустого объекта и присваивание ему идентификатора для поиска в коллекциях.
     * </p>
     *
     * @param id идентификатор
     */
    public abstract void setId(IdClass id);

    /**
     * Сравнивает объекты по идентификатору
     *
     * @param o объект сравнения
     * @return {@code true} если сравнимаемый объект того же типа и идентификаторы равны.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPersistentObject)) return false;

        AbstractPersistentObject that = (AbstractPersistentObject) o;

        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
