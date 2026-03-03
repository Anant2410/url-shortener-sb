package com.url.shortener.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequestDto {
    private String username;
    private String email;
    private Set<String> role;
    private String password;
}
