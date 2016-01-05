package com.mira.jpa2.data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Classifier extends DictionaryObject {
    @Column(nullable = false)
    protected String code;

    public Classifier() {
    }

    public Classifier(Long id) {
        super(id);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
