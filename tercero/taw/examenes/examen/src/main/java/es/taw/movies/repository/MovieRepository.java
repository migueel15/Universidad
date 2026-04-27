package es.taw.movies.repository;

import es.taw.movies.entity.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MovieRepository extends JpaRepository<Movies, Integer> {
}
