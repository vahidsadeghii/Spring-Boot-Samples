package org.example.testcontainer;

import org.example.testcontainer.repository.CityRepository;
import org.example.testcontainer.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.test.context.jdbc.Sql;

@ImportTestcontainers(PostgresqlTestContainerConfigurer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(bootstrapMode = BootstrapMode.DEFAULT)
@Sql(
    scripts = {"classpath:insert_city.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(statements = "delete from city", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class DataTests {
  @Autowired private UserRepository userRepository;
  @Autowired private CityRepository cityRepository;

  @Test
  void withInsertedCities_theFindAll_returnNonEmptyList() {
    Assertions.assertTrue(cityRepository.findAll().size() > 0);
  }

  @Test
  void withEmptyUser_thenFindAll_returnEmptyList() {
    Assertions.assertTrue(userRepository.findAll().isEmpty());
  }

  @Test
  @Sql(
      statements = {"insert  into user_ (id, title, is_active) values(1, 'test', true)"},
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      statements = {"delete from user_"},
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void withInsertOneUser_thenFindAll_returnNonEmptyList() {
    Assertions.assertTrue(userRepository.findAll().size() > 0);
  }
}
