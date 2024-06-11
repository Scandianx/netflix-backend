package com.scandianidev.netflix.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.scandianidev.netflix.dtos.MovieDTO;
import com.scandianidev.netflix.dtos.MovieResponseDTO;
import com.scandianidev.netflix.dtos.MovieSectionDTO;
import com.scandianidev.netflix.dtos.PosterDTO;
import com.scandianidev.netflix.model.Movies;
import com.scandianidev.netflix.model.Usuario;
import com.scandianidev.netflix.repository.MoviesRepository;
import com.scandianidev.netflix.repository.UsuarioRepository;
import com.scandianidev.netflix.security.TokenService;

@Service
public class MoviesService {

    @Autowired
    private MoviesRepository moviesRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FavoritosService favoritosService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;

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
        Pageable pageable = PageRequest.of(1, 10);
        Page<Movies> page = moviesRepository.findAll(pageable);
        List<Movies> moviesList = page.getContent();
        return moviesList;
    }

    public MovieResponseDTO convertToMovieResponseDTO(Movies movie) {
        return modelMapper.map(movie, MovieResponseDTO.class);
    }

    

    public List<Movies> getIndicacoes(List<Integer> genreIds) {
        return moviesRepository.findTop10ByGenreIdsInOrderByPopularityDescVoteAverageDesc(genreIds);
    }

    public List<PosterDTO> getMovieByTitle(String title) {
        List<Movies> movies= moviesRepository.findByTitleContainingIgnoreCase(title);
        List <PosterDTO> dto = new ArrayList<>();
        for (Movies m: movies) {
             dto.add(new PosterDTO(m.getId(), m.getPosterPath()));
             System.out.println(m.getTitle());
        }
        return dto;
        
    }

    public List<MovieSectionDTO> getHomePage(String tokenBearer){
        String token = tokenBearer.replace("Bearer ", "");
        var login = tokenService.validateToken(token);
        Usuario user = usuarioRepository.findByLogin(login);
        List<MovieSectionDTO> sections = new ArrayList<>();
        List <Movies> indicacoes = moviesRepository.findTop10ByGenreIdsInOrderByPopularityDescVoteAverageDesc(favoritosService.favsUser(user.getId()));
        List<MovieDTO> favs = favoritosService.getUserFavs(user.getId());
        Pageable pageable = PageRequest.of(1, 10);
        Page<Movies> page = moviesRepository.findTop10ByOrderByPopularityDesc(pageable);
        List<Movies> top10 = page.getContent();
        Pageable pageable2 = PageRequest.of(1, 10);
        Page<Movies> page2 = moviesRepository.findTop10ByOrderByPopularityDesc(pageable2);
        List<Movies> recent = page2.getContent();
        List<PosterDTO> top10f = favoritosService.getTop10Movies();
        sections.add(new MovieSectionDTO("Seus favoritos", favs));
        sections.add(new MovieSectionDTO("Indicacoes à sua preferência", convertToMovieDTO(indicacoes)));
        sections.add(new MovieSectionDTO("Populares", convertToMovieDTO(top10)));
        sections.add(new MovieSectionDTO("Lançamentos recentes", convertToMovieDTO(recent)));
        List<MovieDTO> top10favs = new ArrayList<>();
        for (PosterDTO p:top10f){
           top10favs.add(new MovieDTO(p.getId(), p.getPoster()));
        }
        sections.add(new MovieSectionDTO("Favoritos da plataforma", top10favs));
        return sections;

        
    }

    private List<MovieDTO> convertToMovieDTO(List<Movies> indicacoes) {
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for (Movies movie : indicacoes) {
            movieDTOList.add(new MovieDTO(movie.getId(),  movie.getPosterPath()));
            }
            return movieDTOList;
        
    }
}
