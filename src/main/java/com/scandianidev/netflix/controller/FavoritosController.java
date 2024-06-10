package com.scandianidev.netflix.controller;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scandianidev.netflix.dtos.FavRequestDTO;
import com.scandianidev.netflix.dtos.MovieResponseDTO;
import com.scandianidev.netflix.service.FavoritosService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("favs")
public class FavoritosController {
    
    @Autowired
    private FavoritosService favoritosService;


    @PostMapping()
    public ResponseEntity<String> postFavoriteMovie(@RequestBody FavRequestDTO entity, @RequestHeader("Authorization") String token) {
        FavRequestDTO dto = new FavRequestDTO(entity.getId(), token);
        return ResponseEntity.ok().body(favoritosService.postFavoriteMovie(dto));
    }

    @GetMapping("/top10")
    public ResponseEntity<List<MovieResponseDTO>> getTop10Movies() {
        return ResponseEntity.ok(favoritosService.getTop10Movies());
    }
    
    
}
