package org.example.testcontainer;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class PostgresqlTestContainerConfigurer {
  // Shared between all test classes
  @Container @ServiceConnection
  static final PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(
          DockerImageName.parse("docker.mofid.dev/postgres:latest")
              .asCompatibleSubstituteFor("postgres"));

  // Per test class
  /*@Container @ServiceConnection
  final PostgreSQLContainer<?> postgreSQLContainer =
          new PostgreSQLContainer<>(
                  DockerImageName.parse("docker.mofid.dev/postgres:latest")
                          .asCompatibleSubstituteFor("postgres"));*/
}
