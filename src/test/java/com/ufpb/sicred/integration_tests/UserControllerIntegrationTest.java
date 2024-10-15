package com.ufpb.sicred.integration_tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufpb.sicred.dto.user.UserDto;
import com.ufpb.sicred.dto.user.UserListDto;
import com.ufpb.sicred.entities.Tipo_usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private UserDto userDto;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        userDto = new UserDto("Cypher", "cypher@gmail.com", "baldtraps");
    }

    @Test
    void createUser() throws Exception {
        //Arrange
        String json = objectMapper.writeValueAsString(userDto);

        //Act
        mockMvc.perform(post("/api/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }


    @Test
    void listUserById() throws Exception {
        //Arrange
        createUser();

        mockMvc.perform(get("/api/usuario/1"))
                .andExpect(status().isOk());
    }

    @Test
    void listUserById_userNotExistent() throws Exception {
        mockMvc.perform(get("/api/usuario/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listUsers() throws Exception {
        mockMvc.perform(get("/api/usuario"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser() throws Exception {
        //Arrange
        createUser();
        UserDto updateUser = new UserDto("Sova", "sova@gmail.com", "iamthehunter");
        String json = objectMapper.writeValueAsString(updateUser);

        UserListDto expectedResponse = new UserListDto(1L, "Sova", "sova@gmail.com", Tipo_usuario.USUARIO);
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);

        //Act
        mockMvc.perform(put("/api/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

    }

    @Test
    void deleteUser() throws Exception {
        //Act
        mockMvc.perform(delete("/api/usuario/1"))
                .andExpect(status().isOk());
    }
}
