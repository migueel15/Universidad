package es.taw.movies.repository;

import es.taw.movies.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<People, Integer> {
}
