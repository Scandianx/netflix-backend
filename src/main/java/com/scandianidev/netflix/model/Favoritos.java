package com.scandianidev.netflix.model;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "favoritos")
public class Favoritos {
    
    @Id
    private String id;
    private int usuarioId;
    private String movieId;
}
