package com.ufpb.sicred.integration_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufpb.sicred.dto.user.UserDto;
import com.ufpb.sicred.dto.user.UserListDto;
import com.ufpb.sicred.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testuser")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private UserDto userDto;
    private Long createdUserId;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userDto = new UserDto("Cypher", "cypher@gmail.com", "baldtraps");

    }

    private Long createUserAndGetId() throws Exception {
        String json = objectMapper.writeValueAsString(userDto);
        MvcResult result = mockMvc.perform(post("/api/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        System.out.println("Location header: " + location); // Adicione esta linha para depuração

        if (location != null && location.matches(".*/(\\d+)$")) { // Verifique se a localização termina com um número
            return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
        } else {
            throw new IllegalStateException("Invalid Location header: " + location);
        }
    }


    @Test
    void createUser() throws Exception {
        createdUserId = createUserAndGetId();
        Assertions.assertNotNull(createdUserId, "O ID do usuário criado deve ser válido.");
    }

    @Test
    void listUserById() throws Exception {
        createdUserId = createUserAndGetId();
        mockMvc.perform(get("/api/usuario/" + createdUserId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Cypher"))
                .andExpect(jsonPath("$.email").value("cypher@gmail.com"));
    }

    @Test
    void listUserById_userNotExistent() throws Exception {
        mockMvc.perform(get("/api/usuario/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser() throws Exception {
        createdUserId = createUserAndGetId();
        UserDto updateUser = new UserDto("Sova", "sova@gmail.com", "iamthehunter");
        String json = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(put("/api/usuario/" + createdUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Sova"))
                .andExpect(jsonPath("$.email").value("sova@gmail.com"));
    }

    @Test
    void deleteUser() throws Exception {
        createdUserId = createUserAndGetId();

        mockMvc.perform(delete("/api/usuario/" + createdUserId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/usuario/" + createdUserId))
                .andExpect(status().isNotFound());
    }
}
