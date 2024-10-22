package com.ufpb.sicred.integration_tests;

import com.ufpb.sicred.controllers.InscricaoController;

import com.ufpb.sicred.model.StatusInscricao;
import com.ufpb.sicred.services.InscricaoService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufpb.sicred.dto.inscricao.InscricaoDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscricaoController.class)
@AutoConfigureMockMvc
public class InscricaoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InscricaoService inscricaoService;

    @MockBean
    private InscricaoDto inscricaoDto;

    @BeforeEach
    public void setup() {
        inscricaoDto = new InscricaoDto();
        inscricaoDto.setUsuarioId(1L);
        inscricaoDto.setEventoId(1L);
        inscricaoDto.setStatus(StatusInscricao.ACEITA);
    }

    @Test
    void createInscricao() throws Exception {
        when(inscricaoService.createInscricao(inscricaoDto)).thenReturn(inscricaoDto); // Ajuste conforme seu método

        mockMvc.perform(post("/api/inscricao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(inscricaoDto)))
                .andExpect(status().isCreated()); // Ajuste para o status correto que você espera
    }

    @Test
    void listAllInscricoes() throws Exception {
        List<InscricaoDto> inscricoes = new ArrayList<>();
        inscricoes.add(inscricaoDto);
        when(inscricaoService.listAll()).thenReturn(inscricoes);

        mockMvc.perform(get("/api/inscricao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].usuarioId").value(inscricaoDto.getUsuarioId()))
                .andExpect(jsonPath("$[0].eventoId").value(inscricaoDto.getEventoId()))
                .andExpect(jsonPath("$[0].status").value(inscricaoDto.getStatus().name())); // Verificando o status
    }

    @Test
    void deleteInscricao() throws Exception {
        Long inscricaoId = 1L; // ID da inscrição a ser deletada

        mockMvc.perform(delete("/api/inscricao/{id}", inscricaoId))
                .andExpect(status().isNoContent()); // Espera o status 204 No Content
    }

    @Test
    void createInscricaoWithInvalidData() throws Exception {
        InscricaoDto invalidInscricaoDto = new InscricaoDto();
        // Deixe o eventoId nulo ou adicione outros dados inválidos
        invalidInscricaoDto.setUsuarioId(1L);
        invalidInscricaoDto.setStatus(StatusInscricao.ACEITA); // Faltando eventoId

        mockMvc.perform(post("/api/inscricao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidInscricaoDto)))
                .andExpect(status().isBadRequest()); // Espera o status 400 Bad Request
    }


    @Test
    void getInscricaoById() throws Exception {
        Long inscricaoId = 1L;
        when(inscricaoService.listInscricao(inscricaoId)).thenReturn(inscricaoDto);

        mockMvc.perform(get("/api/inscricao/{id}", inscricaoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(inscricaoDto.getUsuarioId()))
                .andExpect(jsonPath("$.eventoId").value(inscricaoDto.getEventoId()))
                .andExpect(jsonPath("$.status").value(inscricaoDto.getStatus().name())); // Verificando o status
    }


}
