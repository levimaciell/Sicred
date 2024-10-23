package com.ufpb.sicred.integration_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufpb.sicred.dto.user.UserDto;
import com.ufpb.sicred.dto.user.UserListDto;
import com.ufpb.sicred.entities.Tipo_usuario;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.exceptions.UserNotFoundException;
import com.ufpb.sicred.repositories.UserRepository;
import com.ufpb.sicred.services.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testuser")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository; // Alterado para injetar o repositório real

    @Autowired
    private UserService userService;
    private UserDto userDto;
    private Long createdUserId;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userDto = new UserDto("Cypher", "cypher@gmail.com", "baldtraps");
        if (!userRepository.existsById(1L)) { // Verifique se o usuário nativo já existe
                UserDto nativeUserDto = new UserDto("nativo", "nativo@example.com", "senhaNativa");
            userService.createUser(nativeUserDto);
        }

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

        if (location != null && location.matches(".*/(\\d+)$")) {
            return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
        } else {
            throw new IllegalStateException("Invalid Location header: " + location);
        }
    }

    @Test
    void testCreateUserIntegration() throws Exception {
        String jsonRequest = "{ \"nome\": \"testUser\", \"email\": \"test@example.com\", \"senha\": \"password123\" }";

        mockMvc.perform(post("/api/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        // Verifica se o usuário foi salvo no banco de dados
        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
        assertEquals("testUser", users.get(1).getNome());
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
    void testDeleteUser() throws Exception {
        // Cria um usuário para ser excluído
        UserDto userDto = new UserDto("deleteUser", "delete@example.com", "password123");
        Long createdUserId = userService.createUser(userDto);

        mockMvc.perform(delete("/api/usuario/" + createdUserId))
                .andExpect(status().isNoContent());

        // Verifica se o usuário foi excluído e o nativo ainda está presente
        assertFalse(userRepository.existsById(createdUserId));
        assertTrue(userRepository.existsById(1L)); // Verifique se o usuário nativo ainda está presente
    }


}
