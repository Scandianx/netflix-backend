package com.scandianidev.netflix.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavRequestDTO {
    
    String id;
    String token;

    public FavRequestDTO(String id) {
        this.id = id;
    }
    
}
