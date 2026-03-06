package com.url.shortener.service;

import com.url.shortener.Repository.UrlMappingRepository;
import com.url.shortener.dtos.UrlMappingResponseDto;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingService {

    private UrlMappingRepository urlMappingRepository;

    public UrlMappingResponseDto createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertToDto(savedUrlMapping);
    }
    private UrlMappingResponseDto convertToDto(UrlMapping urlMapping)
    {
        UrlMappingResponseDto response = new UrlMappingResponseDto();
        response.setId(urlMapping.getId());
        response.setOriginalUrl(urlMapping.getOriginalUrl());
        response.setShortUrl(urlMapping.getShortUrl());
        response.setClickCount(urlMapping.getClickCount());
        response.setCreatedDate(urlMapping.getCreatedDate());
        response.setUsername(urlMapping.getUser().getUsername());
        return response;
    }

    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder shortUrl =  new StringBuilder(10);
        for(int i=0;i<10;i++)
        {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }

    public List<UrlMappingResponseDto> getUrlsByUser(User user) {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDto).toList();
    }
}
