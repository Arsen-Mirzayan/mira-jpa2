package com.mira.jpa2.data;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

/**
 * Родительский класс для словарей
 */
@MappedSuperclass
public class DictionaryObject extends DefaultPersistentObject {
    @Lob
    @Column(nullable = false)
    protected String name;

    public DictionaryObject() {
    }

    public DictionaryObject(Long id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
