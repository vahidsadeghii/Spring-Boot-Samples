package com.example.restapi.integration;

import com.example.restapi.config.security.JWTTokenProvider;
import com.example.restapi.config.security.OnlineUser;
import com.example.restapi.controller.create.CreateNewTalkController;
import com.example.restapi.controller.create.CreateNewTalkRequest;
import com.example.restapi.domain.Talk;
import com.example.restapi.service.TalkService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;

@WebMvcTest(value = CreateNewTalkController.class,
        useDefaultFilters = false)
public class CreateNewTaskTest {
  @MockBean private TalkService talkService;
//  @MockBean private OnlineUser onlineUser;
  @MockBean private JWTTokenProvider jwtTokenProvider;
  @Autowired private MockMvc mockMvc;

  @Value("${apis.secure.prefix}")
  private String apiPrefix;

  @BeforeEach
  public void beforeEach() {
    OnlineUser onlineUser = new OnlineUser(10, Collections.singletonList("ADMIN"));
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(onlineUser, "test", onlineUser.getAuthorities()));
  }

  @BeforeEach
  public void initMocks() {
    Mockito.when(
            talkService.createNewTalk(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(
            Talk.builder()
                .id(10)
                .createDate(LocalDateTime.now())
                .link("")
                .likeCount(10)
                .scheduleDate("")
                .build());

//    Mockito.when(onlineUser.getId()).thenReturn(1);
  }

  @Test
//  @WithMockUser(
//      username = "test",
//      roles = {"ADMIN"})
  public void passCorrectRequest_successfully_returnResponse() throws Exception {
    CreateNewTalkRequest createNewTalkRequest = new CreateNewTalkRequest();

    MockHttpServletResponse response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(apiPrefix + "/talks").param()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(createNewTalkRequest)))
            .andReturn()
            .getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }
}
