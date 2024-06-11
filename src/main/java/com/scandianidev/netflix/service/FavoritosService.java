package com.scandianidev.netflix.service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import com.scandianidev.netflix.dtos.FavRequestDTO;
import com.scandianidev.netflix.dtos.MovieDTO;
import com.scandianidev.netflix.dtos.MovieResponseDTO;
import com.scandianidev.netflix.dtos.PosterDTO;
import com.scandianidev.netflix.model.Favoritos;
import com.scandianidev.netflix.model.Usuario;
import com.scandianidev.netflix.model.Movies;
import com.scandianidev.netflix.repository.FavoritosRepository;
import com.scandianidev.netflix.repository.MoviesRepository;
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
    @Autowired
    private MoviesService moviesService;
    @Autowired
    private MoviesRepository moviesRepository;
    

    
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



public List<Integer> favsUser(int id) {
    List<Movies> movies = new ArrayList<>();
    List<Favoritos> favs = favoritosRepository.findAllByUsuarioId(id);

    Map<Integer, Integer> genreCount = new HashMap<>();

    for (Favoritos f : favs) {
        Optional<Movies> movieOpt = moviesRepository.findById(f.getMovieId());
        if (movieOpt.isPresent()) {
            Movies movie = movieOpt.get();
            movies.add(movie);

            List<Integer> genres = movie.getGenreIds();
            for (Integer genre : genres) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
            }
        }
    }

    List<Integer> topGenres = genreCount.entrySet()
                                        .stream()
                                        .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                                        .limit(2)
                                        .map(Map.Entry::getKey)
                                        .collect(Collectors.toList());

    
    

    
    System.out.println("Top 2 genres: " + topGenres);

    return topGenres;
}
    public List<MovieDTO> getUserFavs(int id) {
        List<Movies> movies = new ArrayList<>();
        List<Favoritos> favs = favoritosRepository.findAllByUsuarioId(id);
        // Dois jeitos de percorrer uma lista e atribuir novas variáveis.
        for (Favoritos f : favs) {
            Optional<Movies> movieOpt = moviesRepository.findById(f.getMovieId());
            if (movieOpt.isPresent()) {
                Movies movie = movieOpt.get();
                movies.add(movie);
            }
        }
        // Forma mais inteligente (lambda)
        List<MovieDTO> movieDTOs = movies.stream()
                                     .map(movie -> new MovieDTO(movie.getId(), movie.getPosterPath()))
                                     .collect(Collectors.toList());
        return movieDTOs;


    }


    public List<PosterDTO> getTop10Movies() {
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
    List<PosterDTO> dtos = new ArrayList<>();
    for(String s: top10Movies) {
        MovieResponseDTO m = moviesService.findMovieById(s);
        dtos.add(new PosterDTO(s, m.poster_path()));
    }

    // Aqui você pode usar os IDs dos filmes para buscar detalhes dos filmes e criar os objetos MovieResponseDTO

    return dtos; // Substitua isso pelo retorno dos objetos MovieResponseDTO dos filmes mais populares
}

}
