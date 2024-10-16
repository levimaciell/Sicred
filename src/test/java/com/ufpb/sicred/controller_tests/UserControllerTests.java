package com.ufpb.sicred.controller_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufpb.sicred.controllers.UserController;
import com.ufpb.sicred.dto.user.UserDto;
import com.ufpb.sicred.dto.user.UserListDto;
import com.ufpb.sicred.entities.Tipo_usuario;
import com.ufpb.sicred.exceptions.UserNotFoundException;
import com.ufpb.sicred.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @InjectMocks
    private UserController controller;

    private UserDto dto;

    @BeforeEach
    void setUp(){
        dto = new UserDto("Teste", "teste@gmail.com", "teste123");
    }

    @Test
    void testUpdateUsers() throws Exception {
        //Arrange
        Long id = 1L;
        UserListDto original = new UserListDto(id, "teste1", "teste1@email.com", Tipo_usuario.USUARIO);

        when(service.updateUser(eq(id), any(UserDto.class))).thenReturn(original);

        //ACT
        mockMvc.perform(put("/api/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());

    }

    @Test
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

    @Test
    void testListUser() throws Exception {
        //Arrange
        Long id = 1L;
        UserListDto dto = new UserListDto(id, "teste", "teste@email.com", Tipo_usuario.USUARIO);
        when(service.listUser(id)).thenReturn(dto);

        //Act
        mockMvc.perform(get("/api/usuario/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testListUser_userInexistent() throws Exception {
        //Arrange
        Long id = 1L;
        when(service.listUser(id)).thenThrow(new UserNotFoundException("usuario não encontrado"));

        //Act
        mockMvc.perform(get("/api/usuario/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListAllUsers() throws Exception {
        //Arrange
        Long id = 1L;
        UserListDto dto = new UserListDto(id, "teste", "teste@email.com", Tipo_usuario.USUARIO);
        when(service.listAll()).thenReturn(List.of(dto));

        //Act
        mockMvc.perform(get("/api/usuario"))
                .andExpect(status().isOk());
    }


}
