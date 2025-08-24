package com.devara.urlshortener.controller;

import com.devara.urlshortener.service.UrlShortenerService;
import java.net.URI;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UrlShortenerController {
  private static final String BASE_URL = "http://localhost:8080/";

  private final UrlShortenerService urlShortenerService;

  public UrlShortenerController(UrlShortenerService urlShortenerService) {
    this.urlShortenerService = urlShortenerService;
  }

  /**
   * Endpoint to shorten a given URL.
   *
   * @param request shorten url request containing the original URL, e.g., {"url":
   *     "http://example.com"}
   * @return the shortened URL
   */
  @PostMapping("/shorten")
  public ResponseEntity<String> shortenUrl(@RequestBody Map<String, String> request) {
    String originalUrl = request.get("url");
    boolean useV2 = Boolean.parseBoolean(request.getOrDefault("useV2", "false"));
    if (originalUrl == null || originalUrl.isEmpty()) {
      return ResponseEntity.badRequest().body("Invalid URL");
    }

    String shortenedUrl =
        useV2
            ? urlShortenerService.shortenUrlV2(originalUrl)
            : urlShortenerService.shortenUrl(originalUrl);
    return shortenedUrl != null
        ? ResponseEntity.ok(shortenedUrl)
        : ResponseEntity.status(500).body("Error shortening URL");
  }

  /**
   * Endpoint to redirect a shortened URL to its original URL.
   *
   * @param shortCode the shortened URL path variable
   * @return a redirection to the original URL or an error if not found
   */
  @GetMapping("/{shortCode}")
  public ResponseEntity<?> redirect(@PathVariable String shortCode) {
    if (shortCode == null || shortCode.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    // If found, redirect using 302 for temporary redirect
    // Use 301 for permanent redirect if needed
    return urlShortenerService
        .getOriginalUrl(shortCode)
        .map(originalUrl -> ResponseEntity.status(302).location(URI.create(originalUrl)).build())
        .orElse(ResponseEntity.notFound().build());
  }
}
