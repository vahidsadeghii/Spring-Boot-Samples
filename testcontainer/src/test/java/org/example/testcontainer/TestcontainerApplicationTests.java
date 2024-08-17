package org.example.testcontainer;

import org.example.testcontainer.domain.User;
import org.example.testcontainer.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

@Import(PostgresqlTestContainer.class)
@SpringBootTest
class TestcontainerApplicationTests {
    @Autowired private UserService userService;

    @Test
    @Sql(statements = {"delete from user_;"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void contextLoads() {
        List<User> allUsers = userService.getAllUsers();
        Assertions.assertFalse(allUsers.isEmpty());
    }

}
