package com.scandianidev.netflix.dtos;

import java.util.List;

public class MovieSectionDTO {
    private String title;
    private List<MovieDTO> posters;

    // Construtor
    public MovieSectionDTO(String title, List<MovieDTO> posters) {
        this.title = title;
        this.posters = posters;
    }

    // Getters e Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MovieDTO> getPosters() {
        return posters;
    }

    public void setPosters(List<MovieDTO> posters) {
        this.posters = posters;
    }
}

