package com.example.movie_client.controller;

import com.example.movie_client.constants.Api;
import com.example.movie_client.model.MovieDTO;
import com.example.movie_client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping("movie-details")
public class MovieController {
    @Autowired
    private RestTemplate restTemplate;

    public static String API_GET_MOVIE_DETAILS = Api.baseURL+"/api/movies/details";
    @GetMapping
    public String displayMovieDetailPage(@RequestParam Integer movieId, Model model){
        // Truyền tham số movieId vào query string rồi gửi request
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_GET_MOVIE_DETAILS)
                .queryParam("movieId", "{movieId}")
                .encode()
                .toUriString();
        Map<String, Integer> params = new HashMap<>();
        params.put("movieId", movieId);
        ResponseEntity<MovieDTO> response = restTemplate.getForEntity(urlTemplate,MovieDTO.class,params);
        MovieDTO movie = response.getBody();
        model.addAttribute("movie",movie);
        model.addAttribute("user",new User());
        return "movie-details";
    }
}
