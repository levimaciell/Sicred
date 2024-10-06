package com.ufpb.sicred.controller_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufpb.sicred.controllers.UserController;
import com.ufpb.sicred.dto.user.UserDto;
import com.ufpb.sicred.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    private UserDto dto;

    @BeforeEach
    void setUp(){
        dto = new UserDto("Teste", "teste@gmail.com", "teste123");
    }

    @Test
    //TODO: Consertar erro 500 ao fazer requisição sem body
    void testCreateUser_withPayload() throws Exception {
        mockMvc.perform(post("/api/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateUser_withoutPayload() throws Exception {
        mockMvc.perform(post("/api/usuario"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUser() throws Exception {
        //Arrange
        doNothing().when(service).deleteUser(1L);

        //Act
        mockMvc.perform(delete("/api/usuario/1"))
                .andExpect(status().isOk());
    }

    void testListUser(){

    }

    void testListAllUsers(){

    }

    void testUpdateUsers(){

    }
}
