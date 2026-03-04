package es.tesaw.movies.controller;

import es.tesaw.movies.dao.MoviesRepository;
import es.tesaw.movies.entity.Movies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MoviesController {

    @Autowired
    protected MoviesRepository moviesRepository;

    @GetMapping("/")
    public String doInit (Model model) {

        List< Movies> pelis = this.moviesRepository.findAll();
        model.addAttribute("pelis", pelis);
        return "movies";
    }
}
