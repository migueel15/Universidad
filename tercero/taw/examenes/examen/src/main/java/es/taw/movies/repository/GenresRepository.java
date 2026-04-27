package es.taw.movies.repository;

import es.taw.movies.entity.Genres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenresRepository extends JpaRepository<Genres, Integer> {
}
