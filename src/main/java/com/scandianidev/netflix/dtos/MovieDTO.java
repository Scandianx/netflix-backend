package com.scandianidev.netflix.dtos;

public class MovieDTO {
    private String id;
    private String poster;

    // Construtor
    public MovieDTO(String id, String poster) {
        this.id = id;
        this.poster = poster;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}

