package es.taw.movies.repository;

import es.taw.movies.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PeopleRepository extends JpaRepository<People, Integer> {
  @Query("SELECT p from People p join MovieCrew c on p.id = c.personId.id " +
      "where lower(c.job) like lower(concat('%',?1,'%'))")
  List<People> findByJobAndText(String content);

  @Query("SELECT p from People p join MovieCast c on p.id = c.personId.id " +
      "where lower(c.characterName) like lower(concat('%',?1,'%'))")
  List<People> findByCharacterAndText(String content);
}
