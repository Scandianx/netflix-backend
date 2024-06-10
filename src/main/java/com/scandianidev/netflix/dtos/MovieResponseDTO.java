package com.scandianidev.netflix.dtos;

public record MovieResponseDTO(String _id, int filmeId, String title, String overview, String poster_path, String release_date, double vote_average) {
    
}
