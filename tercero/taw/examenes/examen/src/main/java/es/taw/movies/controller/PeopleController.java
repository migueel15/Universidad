package es.taw.movies.controller;

import es.taw.movies.dto.FilterRequest;
import es.taw.movies.entity.People;
import es.taw.movies.repository.GenresRepository;
import es.taw.movies.repository.MovieRepository;
import es.taw.movies.repository.PeopleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class PeopleController {
    private MovieRepository movieRepository;
    private PeopleRepository peopleRepository;
    private GenresRepository genresRepository;

    PeopleController(MovieRepository movieRepository,
                     PeopleRepository peopleRepository, GenresRepository genresRepository) {
        this.movieRepository = movieRepository;
        this.peopleRepository = peopleRepository;
        this.genresRepository = genresRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model, @ModelAttribute FilterRequest filter) {
        List<People> people;
        if(filter.getType() == FilterRequest.PersonType.REPARTO){
            people =peopleRepository.findByCharacterAndText(filter.getContent());
            System.out.println(people.size());
        }
        else if(filter.getType() == FilterRequest.PersonType.TRABAJADOR){
            people =peopleRepository.findByJobAndText(filter.getContent());
        }else {

            people = peopleRepository.findAll();
        }

        model.addAttribute("people", people);
        model.addAttribute("filter", filter);

        return "people";
    }
}
