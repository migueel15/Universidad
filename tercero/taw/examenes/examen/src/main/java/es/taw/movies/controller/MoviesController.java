package es.taw.movies.controller;

import es.taw.movies.dto.EditMovieRequest;
import es.taw.movies.entity.Genres;
import es.taw.movies.entity.Movies;
import es.taw.movies.entity.People;
import es.taw.movies.repository.GenresRepository;
import es.taw.movies.repository.MovieRepository;
import es.taw.movies.repository.PeopleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MoviesController {
    private MovieRepository movieRepository;
    private GenresRepository genresRepository;

    MoviesController(MovieRepository movieRepository,
                      GenresRepository genresRepository) {
        this.movieRepository = movieRepository;
        this.genresRepository = genresRepository;
    }

    @GetMapping("/movies/{id}")
    public String movieEdit(Model model, @PathVariable("id") Integer movieId) {
        Movies movie = movieRepository.findById(movieId).orElseThrow();
        List<Genres> genres = genresRepository.findAll();
        model.addAttribute("movie", movie);
        model.addAttribute("genres", genres);
        return "movie_detail";
    }

    @PostMapping("/movies/edit")
    public String saveData(@ModelAttribute EditMovieRequest dto){
        Movies movie = movieRepository.findById(dto.getId()).orElseThrow();
        List<Genres> newGenres = genresRepository.findAllById(dto.getGeneros());

        movie.setTitle(dto.getTitulo());
        movie.setRuntime(dto.getDuracion());
        movie.setRevenue(dto.getRecaudacion());
        movie.setGenresList(newGenres);
        movieRepository.save(movie);
        return "redirect:/";
    }
}
