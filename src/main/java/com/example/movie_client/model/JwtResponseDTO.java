package com.example.movie_client.model;

import lombok.Data;

@Data
public class JwtResponseDTO {
    private Integer id;
    private String username;
    private String name;
    private String accessToken;
    private String tokenType;
}
