package com.scandianidev.netflix.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scandianidev.netflix.dtos.MovieResponseDTO;
import com.scandianidev.netflix.model.Movies;
import com.scandianidev.netflix.service.MoviesService;



@RestController
@RequestMapping("movies")
public class MoviesController {
    
    @Autowired
    private MoviesService moviesService;

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> findMovieById (@PathVariable String id) {
        return ResponseEntity.ok(moviesService.findMovieById(id));
        
    }
    
   
    @GetMapping("/movies")
    public List<Movies> getFirst10Movies() {
        return moviesService.getFirst10Movies();
    }
    @GetMapping("/top10")
    public List<Movies> getTop10Movies() {
        return moviesService.getTop10Movies();
    }
    @GetMapping("/indicacao")
    public List<Movies> getIndicacoes() {
        List<Integer> genreIds = new ArrayList<>();
        genreIds.add(35);
        genreIds.add(16);
        return moviesService.getIndicacoes(genreIds);
    }

    @GetMapping("/search/{title}")
    public List<Movies> searchMovies(@PathVariable String title) {
        return moviesService.getMovieByTitle(title);
    }

    
    
}
