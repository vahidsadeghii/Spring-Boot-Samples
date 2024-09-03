package org.example.testcontainer;

import org.example.testcontainer.domain.User;
import org.example.testcontainer.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

//@Import(PostgresqlTestContainer.class)
@SpringBootTest
class TestcontainerApplicationTests {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));


    @Autowired
    private UserService userService;

    @Test
    @Sql(statements = {"delete from user_;"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void contextLoads() {
        List<User> allUsers = userService.getAllUsers();
        Assertions.assertFalse(allUsers.isEmpty());
    }

}
