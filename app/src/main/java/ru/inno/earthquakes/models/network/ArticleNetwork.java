package ru.inno.earthquakes.models.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Artur (gaket) on 2019-11-04.
 */
public class ArticleNetwork {

  @SerializedName("author")
  private String author;
  @SerializedName("title")
  private String title;
  @SerializedName("description")
  private String description;
  @SerializedName("url")
  private String url;
  @SerializedName("urlToImage")
  private String urlToImage;
  @SerializedName("publishedAt")
  private String publishedAt;
  @SerializedName("content")
  private String content;

  public ArticleNetwork(String author, String title, String description, String url,
      String urlToImage, String publishedAt, String content) {
    this.author = author;
    this.title = title;
    this.description = description;
    this.url = url;
    this.urlToImage = urlToImage;
    this.publishedAt = publishedAt;
    this.content = content;
  }

  public String getAuthor() {
    return author;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getUrl() {
    return url;
  }

  public String getUrlToImage() {
    return urlToImage;
  }

  public String getPublishedAt() {
    return publishedAt;
  }

  public String getContent() {
    return content;
  }

}
