package org.example.testcontainer.repository;

import org.example.testcontainer.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {}
