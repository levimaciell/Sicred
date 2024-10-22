package com.ufpb.sicred.integration_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufpb.sicred.dto.event.EventDto;
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
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private EventDto eventDto;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Inicializa um EventDto básico
        eventDto = new EventDto();
        eventDto.setNome("Teste Evento");
        eventDto.setDescricao("Descrição do Evento de Teste");
    }

    @Test
    void createEvent() throws Exception {
        // Arrange
        String json = objectMapper.writeValueAsString(eventDto);

        // Act
        mockMvc.perform(post("/api/evento/user/1") // Passa o userId como parte da URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }


    @Test
    void listEventById() throws Exception {
        // Cria o evento para ter um ID existente
        String json = objectMapper.writeValueAsString(eventDto);
        mockMvc.perform(post("/api/evento/user/1") // Inclui o userId na URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // Busca o evento criado pelo ID 1
        mockMvc.perform(get("/api/evento/1"))
                .andExpect(status().isOk());
    }


    @Test
    void listEvents() throws Exception {
        // Lista todos os eventos, verificando se o status é 200 (Ok)
        mockMvc.perform(get("/api/evento"))
                .andExpect(status().isOk());
    }

    @Test
    void updateEvent() throws Exception {
        // Cria o evento inicialmente
        String json = objectMapper.writeValueAsString(eventDto);
        mockMvc.perform(post("/api/evento/user/1") // Inclui o userId na URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // Prepara um novo DTO para a atualização do evento
        EventDto updateEventDto = new EventDto();
        updateEventDto.setNome("Evento Atualizado");
        updateEventDto.setDescricao("Descrição Atualizada");

        // Transforma o DTO atualizado em JSON
        String updateJson = objectMapper.writeValueAsString(updateEventDto);

        // Executa a requisição de atualização e verifica se o status retornado é 200 (Ok)
        mockMvc.perform(put("/api/evento/user/1/evento/1") // Inclui o userId e eventId na URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }


    @Test
    void deleteEvent() throws Exception {
        // Cria o evento inicialmente
        String json = objectMapper.writeValueAsString(eventDto);
        mockMvc.perform(post("/api/evento/user/1") // Inclui o userId na URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // Deleta o evento com ID 1 e verifica se o status retornado é 204 (No Content)
        mockMvc.perform(delete("/api/evento/user/1/evento/1")) // Inclui o userId e eventId na URL
                .andExpect(status().isNoContent());
    }




}