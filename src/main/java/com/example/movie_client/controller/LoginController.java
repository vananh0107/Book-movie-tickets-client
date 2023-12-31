package com.example.movie_client.controller;

import com.example.movie_client.constants.Api;
import com.example.movie_client.model.JwtResponseDTO;
import com.example.movie_client.model.MovieDTO;
import com.example.movie_client.model.Role;
import com.example.movie_client.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/account")
public class LoginController {
    @Autowired
    private RestTemplate restTemplate;

    public static String apiLogin = Api.baseURL+"/login";
    public static String API_REGISTER = Api.baseURL+"/register";

    @PostMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        try {
            HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
            ResponseEntity<JwtResponseDTO> jwtResponse
                    = restTemplate.exchange(apiLogin, HttpMethod.POST, httpEntity, JwtResponseDTO.class);

            request.getSession().setAttribute("jwtResponse", (JwtResponseDTO) jwtResponse.getBody());
        }
        catch (HttpClientErrorException ex){
            model.addAttribute("loginError",ex.getResponseBodyAsString());
            model.addAttribute("hasLoginErrors", true);
            ResponseEntity<MovieDTO[]> response = restTemplate.getForEntity(HomeController.apiGetShowingMovies, MovieDTO[].class);
            MovieDTO[] movies = response.getBody();
            model.addAttribute("movies", movies);
            model.addAttribute("user",new User());
            model.addAttribute("un",user.getUsername());
            model.addAttribute("pw",user.getPassword());
            return "home";
        }
        return "redirect:/";

    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            Set<Role> roles = new HashSet<>();
            Role roleClient = new Role();
            roleClient.setId(1);
            roleClient.setName("ROLE_CLIENT");
            roles.add(roleClient);
            user.setRoles(roles);
            try {
                HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
                ResponseEntity<JwtResponseDTO> jwtResponse = restTemplate.exchange(API_REGISTER, HttpMethod.POST, httpEntity, JwtResponseDTO.class);
                request.getSession().setAttribute("jwtResponse", jwtResponse.getBody());
            } catch (HttpClientErrorException ex) {
                model.addAttribute("registerError", ex.getResponseBodyAsString());
                model.addAttribute("hasErrors", true);
            }
        }

        ResponseEntity<MovieDTO[]> response = restTemplate.getForEntity(HomeController.apiGetShowingMovies, MovieDTO[].class);
        MovieDTO[] movies = response.getBody();
        model.addAttribute("movies", movies);
        model.addAttribute("user", new User());
        model.addAttribute("fn", user.getFullName());
        model.addAttribute("un", user.getUsername());
        model.addAttribute("pw", user.getPassword());

        return bindingResult.hasErrors() ? "home" : "redirect:/";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request) {
        request.getSession().removeAttribute("jwtResponse");
        return "redirect:/";
    }
}
