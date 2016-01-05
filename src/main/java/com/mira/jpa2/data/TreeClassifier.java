package com.mira.jpa2.data;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Древовидный классификатор
 */
@MappedSuperclass
public class TreeClassifier<T> extends Classifier {
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    protected T parent;
    protected boolean leaf;

    public TreeClassifier() {
    }

    public TreeClassifier(Long id) {
        super(id);
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }
}
