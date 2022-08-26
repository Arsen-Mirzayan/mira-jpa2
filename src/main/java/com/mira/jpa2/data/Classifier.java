package com.mira.jpa2.data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Classifier extends DictionaryObject {
  public static final int CODE_MAX_LENGTH = 200;
  @Column(nullable = false, length = CODE_MAX_LENGTH)
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
