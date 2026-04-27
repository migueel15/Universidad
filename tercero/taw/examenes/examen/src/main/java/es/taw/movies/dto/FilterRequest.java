package es.taw.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterRequest {
  private PersonType type;
  private String content;

  public enum PersonType {
    REPARTO,
    TRABAJADOR
  }
}
