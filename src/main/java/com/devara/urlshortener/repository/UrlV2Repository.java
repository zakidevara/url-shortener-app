package com.devara.urlshortener.repository;

import com.devara.urlshortener.model.UrlV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlV2Repository extends JpaRepository<UrlV2, String> {}
