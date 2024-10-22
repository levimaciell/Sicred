package com.ufpb.sicred.controller_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufpb.sicred.controllers.InscricaoController;
import com.ufpb.sicred.dto.inscricao.InscricaoDto;
import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.model.StatusInscricao;
import com.ufpb.sicred.repositories.EventRepository;
import com.ufpb.sicred.repositories.UserRepository;
import com.ufpb.sicred.services.InscricaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class InscricaoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InscricaoService inscricaoService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private InscricaoController inscricaoController;

    private ObjectMapper objectMapper;

    private InscricaoDto inscricaoDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inscricaoController).build();
        objectMapper = new ObjectMapper();

        // Inicialização do DTO para os testes
        inscricaoDto = new InscricaoDto();
        inscricaoDto.setEventoId(1L);
        inscricaoDto.setUsuarioId(1L); // Assume um ID de usuário existente
        inscricaoDto.setStatus(StatusInscricao.ACEITA);

        // Mockando usuário e evento
        User mockUser = new User();
        mockUser.setId(1L); // ID do usuário existente
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Event mockEvent = new Event();
        mockEvent.setId(1L); // ID do evento existente
        when(eventRepository.findById(1L)).thenReturn(Optional.of(mockEvent));
    }

    @Test
    void createInscricao() throws Exception {
        // Configura o InscricaoDto com valores necessários
        InscricaoDto inscricaoDto = new InscricaoDto();
        inscricaoDto.setUsuarioId(1L); // ID do usuário existente
        inscricaoDto.setEventoId(1L); // ID do evento existente
        inscricaoDto.setStatus(StatusInscricao.ACEITA); // Configurando o status como exigido

        // Mock do serviço para retornar um DTO com ID
        InscricaoDto createdInscricaoDto = new InscricaoDto();
        createdInscricaoDto.setId(1L); // Simulando a criação e atribuição de um ID
        createdInscricaoDto.setUsuarioId(1L);
        createdInscricaoDto.setEventoId(1L);
        createdInscricaoDto.setStatus(StatusInscricao.ACEITA);

        when(inscricaoService.createInscricao(any(InscricaoDto.class))).thenReturn(createdInscricaoDto);

        mockMvc.perform(post("/api/inscricao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscricaoDto)))
                .andExpect(status().isCreated()) // Ajusta para esperar 201 Created
                .andExpect(jsonPath("$.id").value(createdInscricaoDto.getId())); // Verifica se o ID está presente
    }




    @Test
    void listInscricaoById() throws Exception {
        InscricaoDto inscricaoDto = new InscricaoDto();
        inscricaoDto.setId(1L); // ID existente
        inscricaoDto.setUsuarioId(1L);
        inscricaoDto.setEventoId(1L);
        inscricaoDto.setStatus(StatusInscricao.ACEITA);

        when(inscricaoService.listInscricao(1L)).thenReturn(inscricaoDto);

        mockMvc.perform(get("/api/inscricao/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(inscricaoDto.getId()));
    }

    @Test
    void deleteInscricao() throws Exception {
        // Deleta a inscrição com ID 1 e verifica se o status retornado é 204 (No Content)
        mockMvc.perform(delete("/api/inscricao/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    void listAllInscricoes() throws Exception {
        // Cria um InscricaoDto para o teste
        InscricaoDto inscricaoDto = new InscricaoDto();
        inscricaoDto.setId(1L); // ID da inscrição
        inscricaoDto.setUsuarioId(1L); // ID do usuário
        inscricaoDto.setEventoId(1L); // ID do evento
        inscricaoDto.setStatus(StatusInscricao.ACEITA); // Status da inscrição

        // Prepara a lista com a inscrição
        List<InscricaoDto> inscricoes = new ArrayList<>();
        inscricoes.add(inscricaoDto);
        when(inscricaoService.listAll()).thenReturn(inscricoes);

        // Realiza a requisição GET e verifica as expectativas
        mockMvc.perform(get("/api/inscricao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // Verifica que a lista contém 1 item
                .andExpect(jsonPath("$[0].id").value(inscricaoDto.getId())) // Verifica o ID da inscrição
                .andExpect(jsonPath("$[0].usuarioId").value(inscricaoDto.getUsuarioId())) // Verifica o ID do usuário
                .andExpect(jsonPath("$[0].eventoId").value(inscricaoDto.getEventoId())) // Verifica o ID do evento
                .andExpect(jsonPath("$[0].status").value(inscricaoDto.getStatus().toString())); // Verifica o status
    }


}