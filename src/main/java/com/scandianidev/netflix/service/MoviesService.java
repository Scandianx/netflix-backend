package com.scandianidev.netflix.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.scandianidev.netflix.dtos.MovieDTO;
import com.scandianidev.netflix.dtos.MovieResponseDTO;
import com.scandianidev.netflix.dtos.MovieSectionDTO;
import com.scandianidev.netflix.dtos.PosterDTO;
import com.scandianidev.netflix.model.Favoritos;
import com.scandianidev.netflix.model.Movies;
import com.scandianidev.netflix.model.Usuario;
import com.scandianidev.netflix.repository.MoviesRepository;
import com.scandianidev.netflix.repository.UsuarioRepository;
import com.scandianidev.netflix.repository.FavoritosRepository;
import com.scandianidev.netflix.security.TokenService;

@Service
public class MoviesService {

    @Autowired
    private MoviesRepository moviesRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private FavoritosRepository favoritosRepository;

    public MovieResponseDTO findMovieById(String id) {
        Optional<Movies> movie = moviesRepository.findById(id);
        if (movie.isPresent()) {
            Movies movie1 = movie.get();
            return new MovieResponseDTO(id, movie1.getFilmeId(), movie1.getTitle(), movie1.getOverview(),
                    movie1.getPosterPath(), movie1.getReleaseDate(), movie1.getVoteAverage());

        } else {
            System.out.println("Nao achou");
            return null;
        }
    }

    public List<Movies> getFirst10Movies() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Movies> page = moviesRepository.findAll(pageable);
        List<Movies> moviesList = page.getContent();
        return moviesList;
    }

    public List<Movies> getIndicacoes(List<Integer> genreIds) {
        return moviesRepository.findTop30ByGenreIdsInOrderByPopularityDescVoteAverageDesc(genreIds);
    }

    public List<PosterDTO> getMovieByTitle(String title) {
        List<Movies> movies = moviesRepository.findByTitleContainingIgnoreCase(title);
        List<PosterDTO> dto = new ArrayList<>();
        for (Movies m : movies) {
            dto.add(new PosterDTO(m.getId(), m.getPosterPath()));
            System.out.println(m.getTitle());
        }
        return dto;
    }

    public List<MovieSectionDTO> getHomePage(String tokenBearer) {
        String token = tokenBearer.replace("Bearer ", "");
        var login = tokenService.validateToken(token);
        Usuario user = usuarioRepository.findByLogin(login);
        List<MovieSectionDTO> sections = new ArrayList<>();
        
        List<MovieDTO> favs = getUserFavs(user.getId());
    
        Pageable pageable = PageRequest.of(0, 10);
        Page<Movies> page = moviesRepository.findTop10ByOrderByPopularityDesc(pageable);
        List<Movies> top10 = page.getContent();
    
        Pageable pageable2 = PageRequest.of(1, 10);
        Page<Movies> page2 = moviesRepository.findTop10ByOrderByPopularityDesc(pageable2);
        List<Movies> recent = page2.getContent();
    
        List<PosterDTO> top10f = getTop10Movies();
        List<String> usedMovieIds = favs.stream().map(MovieDTO::getId).collect(Collectors.toList());
        usedMovieIds.addAll(top10.stream().map(Movies::getId).collect(Collectors.toList()));
        usedMovieIds.addAll(recent.stream().map(Movies::getId).collect(Collectors.toList()));
    
        List<Movies> indicacoes = moviesRepository.findTop30ByGenreIdsInOrderByPopularityDescVoteAverageDesc(favsUser(user.getId()))
                .stream()
                .filter(movie -> !usedMovieIds.contains(movie.getId()))
                .collect(Collectors.toList());
    
        sections.add(new MovieSectionDTO("Seus favoritos", favs));
        sections.add(new MovieSectionDTO("Indicações à sua preferência", convertToMovieDTO(indicacoes)));
        sections.add(new MovieSectionDTO("Populares", convertToMovieDTO(top10)));
        sections.add(new MovieSectionDTO("Lançamentos recentes", convertToMovieDTO(recent)));
    
        List<MovieDTO> top10favs = new ArrayList<>();
        for (PosterDTO p : top10f) {
            top10favs.add(new MovieDTO(p.getId(), p.getPoster()));
        }
        sections.add(new MovieSectionDTO("Favoritos da plataforma", top10favs));
    
        return sections;
    }

    private List<MovieDTO> convertToMovieDTO(List<Movies> indicacoes) {
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for (Movies movie : indicacoes) {
            movieDTOList.add(new MovieDTO(movie.getId(), movie.getPosterPath()));
        }
        return movieDTOList;
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
        for (Favoritos f : favs) {
            Optional<Movies> movieOpt = moviesRepository.findById(f.getMovieId());
            if (movieOpt.isPresent()) {
                Movies movie = movieOpt.get();
                movies.add(movie);
            }
        }
        List<MovieDTO> movieDTOs = movies.stream()
                                         .map(movie -> new MovieDTO(movie.getId(), movie.getPosterPath()))
                                         .collect(Collectors.toList());
        return movieDTOs;
    }

    public List<PosterDTO> getTop10Movies() {
        List<Favoritos> favs = favoritosRepository.findAll();
        Map<String, Integer> movieCountMap = new HashMap<>();

        for (Favoritos fav : favs) {
            String movieId = fav.getMovieId();
            movieCountMap.put(movieId, movieCountMap.getOrDefault(movieId, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(movieCountMap.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        List<String> top10Movies = new ArrayList<>();
        for (int i = 0; i < Math.min(10, sortedEntries.size()); i++) {
            top10Movies.add(sortedEntries.get(i).getKey());
        }
        List<PosterDTO> dtos = new ArrayList<>();
        for (String s : top10Movies) {
            MovieResponseDTO m = findMovieById(s);
            dtos.add(new PosterDTO(s, m.poster_path()));
        }

        return dtos;
    }
}
