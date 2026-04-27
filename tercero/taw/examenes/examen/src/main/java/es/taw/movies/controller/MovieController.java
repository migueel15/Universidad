package es.taw.movies.controller;

import es.taw.movies.entity.Genres;
import es.taw.movies.entity.Movies;
import es.taw.movies.entity.People;
import es.taw.movies.repository.GenresRepository;
import es.taw.movies.repository.MovieRepository;
import es.taw.movies.repository.PeopleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MovieController {
    private MovieRepository movieRepository;
    private PeopleRepository peopleRepository;
    private GenresRepository genresRepository;

    MovieController(MovieRepository movieRepository,
                    PeopleRepository peopleRepository, GenresRepository genresRepository) {
        this.movieRepository = movieRepository;
        this.peopleRepository = peopleRepository;
        this.genresRepository = genresRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<People> people = peopleRepository.findAll();
        System.out.println(people.size());
        model.addAttribute("people", people);

        return "movies";
    }

    @GetMapping("/movies/{id}")
    public String dashboard(Model model, @PathVariable("id") Integer movieId) {
        Movies movie = movieRepository.findById(movieId).orElseThrow();
        List<Genres> genres = genresRepository.findAll();
        model.addAttribute("movie", movie);
        model.addAttribute("genres", genres);
        return "movie_detail";
    }
}
