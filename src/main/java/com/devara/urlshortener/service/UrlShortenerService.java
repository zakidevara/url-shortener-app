package com.devara.urlshortener.service;

import com.devara.urlshortener.algorithm.Base62;
import com.devara.urlshortener.algorithm.Hash256;
import com.devara.urlshortener.model.Url;
import com.devara.urlshortener.model.UrlV2;
import com.devara.urlshortener.repository.UrlRepository;
import com.devara.urlshortener.repository.UrlV2Repository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UrlShortenerService.class);
  private final UrlRepository urlRepository;
  private final UrlV2Repository urlV2Repository;

  @Value("${app.base-url}")
  private String baseUrl;

  public UrlShortenerService(UrlRepository urlRepository, UrlV2Repository urlV2Repository) {
    this.urlRepository = urlRepository;
    this.urlV2Repository = urlV2Repository;
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

  public String shortenUrlV2(String originalUrl) {
    try {
      // Check if the URL already exists in the repository
      String fullHash = Hash256.hash(originalUrl);
      String shortCode = null;
      for (int i = 0; i <= fullHash.length() - 7; i++) {
        String potentialShortCode = fullHash.substring(i, i + 7);
        Optional<UrlV2> existingUrl = urlV2Repository.findById(potentialShortCode);

        if (existingUrl.isEmpty()) {
          shortCode = potentialShortCode;
          urlV2Repository.save(new UrlV2(shortCode, originalUrl));
          break;
        } else if (existingUrl.get().getOriginalUrl().equals(originalUrl)) {
          shortCode = potentialShortCode;
          return baseUrl + "/" + shortCode;
        }
        // If collision occurs, continue to the next substring
      }

      if (shortCode == null) {
        return null;
      }
      return baseUrl + "/" + shortCode;
    } catch (Exception e) {
      LOGGER.error("Error shortening URL: {}", e.getMessage());
      throw new RuntimeException("Error shortening URL", e);
    }
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
      return urlRepository
          .findById(id)
          .map(Url::getOriginalUrl)
          .or(
              () -> {
                ;
                // If not found in Url repository, check UrlV2 repository
                return urlV2Repository.findById(shortCode).map(UrlV2::getOriginalUrl);
              });
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
