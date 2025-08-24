package com.devara.urlshortener.service;

import com.devara.urlshortener.algorithm.Base62;
import com.devara.urlshortener.model.Url;
import com.devara.urlshortener.repository.UrlRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UrlShortenerService.class);
  private final UrlRepository urlRepository;

  @Value("${app.base-url}")
  private String baseUrl;

  public UrlShortenerService(UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  /**
   * Shortens the given original URL and returns the shortened URL.
   *
   * @param originalUrl the original URL to shorten
   * @return the shortened URL
   */
  public String shortenUrl(String originalUrl) {
    // Logic to shorten the URL and save it to the repository
    // This is a placeholder implementation
    Url urlEntity = new Url();
    urlEntity.setOriginalUrl(originalUrl);
    var url = urlRepository.save(urlEntity);
    return baseUrl + "/" + Base62.encode(url.getId());
  }

  /**
   * Retrieves the original URL for the given shortened URL code.
   *
   * @param shortCode the shortened URL code
   * @return an Optional containing the original URL if found, or empty if not found
   */
  public Optional<String> getOriginalUrl(String shortCode) {
    try {
      Long id = Base62.decode(shortCode);
      return urlRepository.findById(id).map(Url::getOriginalUrl);
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
