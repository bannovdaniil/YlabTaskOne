package io.ylab.intensive.lesson04.movie.model;

public class Movie {
  private Integer year;
  private Integer length;
  private String title;
  private String subject;
  private String actors;
  private String actress;
  private String director;
  private Integer popularity;
  private Boolean awards;

  public Movie() {
  }

  public Movie setYear(Integer year) {
    this.year = year;
    return this;
  }

  public Movie setLength(Integer length) {
    this.length = length;
    return this;
  }

  public Movie setTitle(String title) {
    this.title = title;
    return this;
  }

  public Movie setSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public Movie setActors(String actors) {
    this.actors = actors;
    return this;
  }

  public Movie setActress(String actress) {
    this.actress = actress;
    return this;
  }

  public Movie setDirector(String director) {
    this.director = director;
    return this;
  }

  public Movie setPopularity(Integer popularity) {
    this.popularity = popularity;
    return this;
  }

  public Movie setAwards(Boolean awards) {
    this.awards = awards;
    return this;
  }

  public Integer getYear() {
    return year;
  }

  public Integer getLength() {
    return length;
  }

  public String getTitle() {
    return title;
  }

  public String getSubject() {
    return subject;
  }

  public String getActors() {
    return actors;
  }

  public String getActress() {
    return actress;
  }

  public String getDirector() {
    return director;
  }

  public Integer getPopularity() {
    return popularity;
  }

  public Boolean getAwards() {
    return awards;
  }
}
