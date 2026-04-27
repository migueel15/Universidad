package es.taw.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.util.List;

@Getter
@AllArgsConstructor
public class EditMovieRequest {
  private Integer id;
  private String titulo;
  private Double duracion;
  private BigInteger recaudacion;
  private List<Integer> generos;
}
