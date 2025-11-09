package org.example.testcontainer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportTestcontainers(PostgresqlTestContainerConfigurer.class)
public class UserControllerIntegrationTest {
  @LocalServerPort private int port;

  @BeforeEach
  void setup() {
    RestAssured.port = port;
  }

  @Test
  @Sql(
      statements = {"insert  into user_ (id, title, is_active) values(1, 'test', true)"},
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      statements = {"delete from user_"},
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void withNewUsers_theCallUsers_returnUsers() {
    RestAssured.given().contentType(ContentType.JSON).when().get("/users").then().statusCode(200);
  }
}
