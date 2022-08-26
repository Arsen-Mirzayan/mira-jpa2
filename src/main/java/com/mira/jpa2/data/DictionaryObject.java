package com.mira.jpa2.data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Родительский класс для словарей
 */
@MappedSuperclass
public class DictionaryObject extends DefaultPersistentObject {
  /**
   * Максимально допустимая длина поля name
   */
  public static final int NAME_MAX_LENGTH = 2000;

  @Column(nullable = false, length = NAME_MAX_LENGTH)
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
