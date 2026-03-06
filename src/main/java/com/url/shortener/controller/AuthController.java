package com.url.shortener.controller;

import com.url.shortener.dtos.LoginRequestDto;
import com.url.shortener.dtos.RegisterRequestDto;
import com.url.shortener.models.User;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto request)
    {
        return ResponseEntity.ok(userService.authenticateUser(request));
    }


    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto request){

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole("ROLE_USER");
        user.setEmail(request.getEmail());
        userService.registerUser(user);
        return ResponseEntity.ok("User Registered Successfully!");
    }
}
