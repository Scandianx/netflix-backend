package com.scandianidev.netflix.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movies")
public class Movies {
    @Id
    private String id;
    
    @Field("filmeId")
    private int filmeId;
    
    @Field("title")
    private String title;
    
    @Field("overview")
    private String overview;
    
    @Field("poster_path")
    private String posterPath;
    
    @Field("release_date")
    private String releaseDate;
    
    @Field("vote_average")
    private double voteAverage;
    
    @Field("vote_count")
    private int voteCount;
    
    @Field("popularity")
    private double popularity;
    
    @Field("adult")
    private boolean adult;
    
    @Field("genre_ids")
    private List<Integer> genreIds;
    
    @Field("__v")
    private int version;
}
