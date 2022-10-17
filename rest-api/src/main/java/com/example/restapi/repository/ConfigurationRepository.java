package com.example.restapi.repository;

import com.example.restapi.domain.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, String> {
}
