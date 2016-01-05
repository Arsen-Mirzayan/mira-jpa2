package com.mira.jpa2.data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Родитель всех объектов с простым автогенерируемым ключом.
 */
@MappedSuperclass
public class DefaultPersistentObject extends AbstractPersistentObject<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * Конструктор по умолчанию
     */
    public DefaultPersistentObject() {
    }


    public DefaultPersistentObject(Long id) {
        this.id = id;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }


}
