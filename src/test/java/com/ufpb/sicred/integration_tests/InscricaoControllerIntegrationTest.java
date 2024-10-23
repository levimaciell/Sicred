package com.ufpb.sicred.integration_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufpb.sicred.dto.inscricao.InscricaoDto;
import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.entities.Tipo_usuario;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.model.Status;
import com.ufpb.sicred.model.StatusInscricao;
import com.ufpb.sicred.repositories.EventRepository;
import com.ufpb.sicred.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testuser")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InscricaoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private InscricaoDto inscricaoDto;
    private Long createdUserId;
    private Long createdEventId;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        // Criação de um usuário
        User user = new User(null, "Cypher_" + UUID.randomUUID(), "cypher@gmail.com", "baldtraps", Tipo_usuario.USUARIO);
        user = userRepository.save(user);
        createdUserId = user.getId();

        // Criação de um evento
        Event event = new Event(null, "Evento Teste", "Descrição do Evento", LocalDateTime.now(),
                LocalDateTime.now().plusHours(2), "Local do Evento", 100,
                Status.ATIVO, LocalDateTime.now(), user);
        event = eventRepository.save(event);
        createdEventId = event.getId();

        // Inicializa o DTO de inscrição
        inscricaoDto = new InscricaoDto();
        inscricaoDto.setUsuarioId(createdUserId);
        inscricaoDto.setEventoId(createdEventId);
        inscricaoDto.setStatus(StatusInscricao.ACEITA);
    }

    @Test
    void createInscricao() throws Exception {
        String json = objectMapper.writeValueAsString(inscricaoDto);

        mockMvc.perform(post("/api/inscricao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.usuarioId").value(createdUserId))
                .andExpect(jsonPath("$.eventoId").value(createdEventId))
                .andExpect(jsonPath("$.status").value(StatusInscricao.ACEITA.toString()));
    }


    @Test
    void listInscricaoById() throws Exception {
        // Criação da inscrição
        String json = objectMapper.writeValueAsString(inscricaoDto);
        String response = mockMvc.perform(post("/api/inscricao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extrair o ID da inscrição criada
        InscricaoDto createdInscricao = objectMapper.readValue(response, InscricaoDto.class);
        Long createdInscricaoId = createdInscricao.getId(); // Assumindo que seu DTO tem um método getId()

        // Recuperar a inscrição pelo ID
        mockMvc.perform(get("/api/inscricao/" + createdInscricaoId)) // Usa o ID real
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.usuarioId").value(createdUserId))
                .andExpect(jsonPath("$.eventoId").value(createdEventId))
                .andExpect(jsonPath("$.status").value(StatusInscricao.ACEITA.toString()));
    }

    @Test
    public void testDeleteInscricao() throws Exception {
        // Criar a inscrição antes de tentar deletar
        String json = objectMapper.writeValueAsString(inscricaoDto);
        String response = mockMvc.perform(post("/api/inscricao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extrair o ID da inscrição criada
        InscricaoDto createdInscricao = objectMapper.readValue(response, InscricaoDto.class);
        Long createdInscricaoId = createdInscricao.getId(); // Assumindo que o DTO tem getId()

        // Deletar a inscrição recém-criada
        mockMvc.perform(delete("/api/inscricao/" + createdInscricaoId))
                .andExpect(status().isNoContent());
    }
    @Test
    void listInscricaoById_notExistent() throws Exception {
        mockMvc.perform(get("/api/inscricao/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Inscrição não encontrada"));
    }



}