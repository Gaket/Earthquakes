package ru.inno.earthquakes.models.entities;

import java.net.URL;

/**
 * Created by Artur (gaket) on 2019-11-04.
 */
public class News {

  private String title;
  private String description;
  private String picture;
  private String url;

  public News() {
    
  }

  public News(String title, String description, String picture, String url) {
    this.title = title;
    this.description = description;
    this.picture = picture;
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getPicture() {
    return picture;
  }

  public String getUrl() {
    return url;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
