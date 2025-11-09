package org.example.testcontainer;

import org.example.testcontainer.controller.UserController;
import org.example.testcontainer.domain.User;
import org.example.testcontainer.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationMockingTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private UserService userService;

  @Test
  void withSomeMockUsers_callGetUsers_returnListsNotEmpty() throws Exception {
    Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(new User(), new User()));
    ObjectMapper objectMapper = new ObjectMapper();
    List<User> result =
        objectMapper.readValue(
            mockMvc
                .perform(MockMvcRequestBuilders.get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
    Assertions.assertTrue(result.size() > 0);
  }
}
