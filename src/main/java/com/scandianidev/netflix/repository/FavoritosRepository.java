package com.scandianidev.netflix.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.scandianidev.netflix.dtos.AggregationResult;

import com.scandianidev.netflix.model.Favoritos;

public interface FavoritosRepository extends MongoRepository<Favoritos, String>{
    List<Favoritos> findAllByUsuarioId (int id);
    @Aggregation(pipeline = {
        "{ '$group': { '_id': '$movieId', 'count': { '$sum': 1 } } }",
        "{ '$sort': { 'count': -1 } }",
        "{ '$limit': 10 }"
    })
    List<AggregationResult> findTop10Movies();
}
