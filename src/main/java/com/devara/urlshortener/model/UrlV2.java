package com.devara.urlshortener.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlV2 {
  @Id private String shortCode;

  @Column(columnDefinition = "TEXT", nullable = false, length = 2048)
  private String originalUrl;
}
