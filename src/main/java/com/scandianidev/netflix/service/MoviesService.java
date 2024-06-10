package com.scandianidev.netflix.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.scandianidev.netflix.dtos.MovieResponseDTO;
import com.scandianidev.netflix.model.Movies;
import com.scandianidev.netflix.repository.MoviesRepository;

@Service
public class MoviesService {

    @Autowired
    private MoviesRepository moviesRepository;
    @Autowired
    private ModelMapper modelMapper;

    public MovieResponseDTO findMovieById(String id) {
        Optional<Movies> movie = moviesRepository.findById(id);
        if (movie.isPresent()) {
            Movies movie1 = movie.get();
            return new MovieResponseDTO(id, movie1.getFilmeId(), movie1.getTitle(), movie1.getOverview(),
                    movie1.getPosterPath(), movie1.getReleaseDate(), movie1.getVoteAverage());

        } else {
            System.out.println("Nao achou");
            return null;
        }
    }

    public List<Movies> getFirst10Movies() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Movies> page = moviesRepository.findAll(pageable);
        List<Movies> moviesList = page.getContent();
        return moviesList;
    }

    public MovieResponseDTO convertToMovieResponseDTO(Movies movie) {
        return modelMapper.map(movie, MovieResponseDTO.class);
    }

    public List<Movies> getTop10Movies () {
        System.out.println("e");
        return moviesRepository.findTop10ByOrderByPopularityDesc();
    }

    public List<Movies> getIndicacoes(List<Integer> genreIds) {
        return moviesRepository.findTop10ByGenreIdsInOrderByPopularityDescVoteAverageDesc(genreIds);
    }

    public List<Movies> getMovieByTitle(String title) {
        return moviesRepository.findByTitleContainingIgnoreCase(title);
    }
}
