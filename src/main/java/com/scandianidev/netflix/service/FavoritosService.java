package com.scandianidev.netflix.service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import com.scandianidev.netflix.dtos.FavRequestDTO;
import com.scandianidev.netflix.dtos.MovieResponseDTO;
import com.scandianidev.netflix.model.Favoritos;
import com.scandianidev.netflix.model.Usuario;
import com.scandianidev.netflix.repository.FavoritosRepository;
import com.scandianidev.netflix.repository.UsuarioRepository;
import com.scandianidev.netflix.security.TokenService;


@Service
public class FavoritosService {
    
    @Autowired
    private FavoritosRepository favoritosRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;
    

    
    public String postFavoriteMovie(@RequestBody FavRequestDTO entity) {
          String userTokeString = entity.getToken().replace("Bearer ", "");
          System.out.println(userTokeString);
          var login = tokenService.validateToken(userTokeString);
          Usuario user = usuarioRepository.findByLogin(login);
          if(user == null){
            
             return null;
          }
          List<Favoritos> favs =favoritosRepository.findAllByUsuarioId(user.getId());
          for (Favoritos f: favs) {
            if(f.getMovieId().equals(entity.getId())){
                favoritosRepository.deleteById(f.getId());
                return "Filme removido dos favoritos";
            }
          }
          
          Favoritos favoritos = new Favoritos();
          favoritos.setUsuarioId(user.getId());
          favoritos.setMovieId(entity.getId());
          favoritosRepository.save(favoritos);
          return "Filme adicionado a lista de favoritos!";


    }
    

    public List<MovieResponseDTO> getTop10Movies() {
    List<Favoritos> favs = favoritosRepository.findAll();
    Map<String, Integer> movieCountMap = new HashMap<>();

    // Contar a quantidade de vezes que cada filme aparece nos favoritos
    for (Favoritos fav : favs) {
        String movieId = fav.getMovieId();
        movieCountMap.put(movieId, movieCountMap.getOrDefault(movieId, 0) + 1);
    }

    // Ordenar o mapa pela contagem de favoritos em ordem decrescente
    List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(movieCountMap.entrySet());
    sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

    // Pegar os 10 filmes mais populares
    List<String> top10Movies = new ArrayList<>();
    for (int i = 0; i < Math.min(10, sortedEntries.size()); i++) {
        top10Movies.add(sortedEntries.get(i).getKey());
    }
    for(String s: top10Movies) {
        System.out.println(s);
    }

    // Aqui você pode usar os IDs dos filmes para buscar detalhes dos filmes e criar os objetos MovieResponseDTO

    return null; // Substitua isso pelo retorno dos objetos MovieResponseDTO dos filmes mais populares
}

}
