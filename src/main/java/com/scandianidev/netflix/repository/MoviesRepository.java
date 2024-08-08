package com.scandianidev.netflix.repository;



import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import com.scandianidev.netflix.model.Movies;

public interface MoviesRepository extends MongoRepository<Movies, String>{
    Page<Movies> findAll(Pageable pageable);

    @Query(value = "{}", sort = "{ 'popularity' : -1 }")
    Page<Movies> findTop10ByOrderByPopularityDesc(Pageable pageable);

    @Query(value = "{ 'genreIds': { $in: ?0 } }")
    List<Movies> findByGenreIdsIn(List<Integer> genreIds);

    List<Movies> findTop30ByGenreIdsInOrderByPopularityDescVoteAverageDesc(List<Integer> genreIds);

    List<Movies> findByTitleContainingIgnoreCase(String title);
    @Query(value = "{}", sort = "{ 'release_date' : -1 }")
    Page<Movies> findTop10ByReleaseDateDesc(Pageable pageable);

    
    
}
