package org.exoplatform.bookstore.entity;

public class Book {

  private String id;
  
  private String title;

  private long   price;

  public Book() {

  }

  public Book(String title, long price) {
    this.title = title;
    this.price = price;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public long getPrice() {
    return price;
  }

  public void setPrice(long price) {
    this.price = price;
  }

}