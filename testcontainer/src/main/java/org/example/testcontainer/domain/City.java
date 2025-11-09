package org.example.testcontainer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class City {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String detail;

  public City() {}

  public City(long id, String detail) {
    this.id = id;
    this.detail = detail;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }
}
