package org.example.testcontainer;

import org.example.testcontainer.domain.City;
import org.example.testcontainer.domain.User;
import org.example.testcontainer.repository.CityRepository;
import org.example.testcontainer.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@ImportTestcontainers(PostgresqlTestContainerConfigurer.class)
@SpringBootTest
@Sql(scripts = "classpath:insert_city.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(statements = "delete from city", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class UserServiceIntegrationTests {
  @Autowired private UserService userService;
  @Autowired private CityRepository cityRepository;

  @Test
  @Order(1)
  @Sql(
      statements = {"delete from user_;"},
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @Sql(
      statements = {"insert into user_(id, title, is_active) values (1, 'vahid', true)"},
      //      scripts = "classpath:insert_users.sql",
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void insertSomeUsers_thenFindAllUsers_thenReturnCorrectList() {
    List<User> allUsers = userService.getAllUsers();
    Assertions.assertEquals(1, allUsers.size());
  }

  @Test
  @Order(2)
  void emptyUsers_thenGetAllUsers_thenResultShouldBeEmpty() {
    List<User> allUsers = userService.getAllUsers();
    Assertions.assertTrue(allUsers.isEmpty());
  }

  @Test
  void insertCities_getAllCities_thenReturnNonEmptyList() {
    List<City> cities = cityRepository.findAll();
    Assertions.assertEquals(1, cities.size());
  }
}
