package com.url.shortener.controller;

import com.url.shortener.dtos.ClickEventResponseDto;
import com.url.shortener.dtos.UrlMappingResponseDto;
import com.url.shortener.models.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {

    private UrlMappingService urlMappingService;
    private UserService userService;

    //{"originalUrl" : "https://example.com"}
    //https://abc.com/ICCBaYfl0i --> https://example.com
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingResponseDto> createShortUrl(@RequestBody Map<String, String> request,
                                                                Principal principal)
    {
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());

        //call service
        UrlMappingResponseDto response  = urlMappingService.createShortUrl(originalUrl, user);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingResponseDto>> getUserUrls(Principal principal)
    {
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingResponseDto> list = urlMappingService.getUrlsByUser(user);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventResponseDto>> getUrlAnalytics(@PathVariable String shortUrl,
                                                                       @RequestParam("startDate") String startDate,
                                                                       @RequestParam(value = "endDate", required = false) String endDate)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end;
        if (endDate == null || endDate.isEmpty()) {
            end = LocalDateTime.now(); // 🔥 current time
        } else {
            end = LocalDateTime.parse(endDate, formatter);
        }

        List<ClickEventResponseDto> list = urlMappingService.getClickEventByDate(shortUrl, start, end);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal,
                                                                     @RequestParam("startDate") String startDate,
                                                                     @RequestParam("endDate") String endDate)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        User user = userService.findByUsername(principal.getName());
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClicksByUserAndDate(user, start, end);
        return ResponseEntity.ok(totalClicks);
    }
}
