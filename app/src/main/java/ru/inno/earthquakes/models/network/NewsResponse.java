package ru.inno.earthquakes.models.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Artur Badretdinov (Gaket) 20.07.17.
 */
public class NewsResponse {

  @SerializedName("status")
  private String status;
  @SerializedName("totalResults")
  private Integer totalResults;
  @SerializedName("articles")
  private List<ArticleNetwork> articles;

  public NewsResponse(String status, Integer totalResults,
      List<ArticleNetwork> articles) {
    this.status = status;
    this.totalResults = totalResults;
    this.articles = articles;
  }

  public String getStatus() {
    return status;
  }

  public Integer getTotalResults() {
    return totalResults;
  }

  public List<ArticleNetwork> getArticles() {
    return articles;
  }

}
