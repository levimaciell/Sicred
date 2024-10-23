package com.ufpb.sicred.controller_tests;
/*testListEvents(): Testa o endpoint para listar todos os eventos, garantindo que a lista retorne os eventos corretos.

testGetEvent(): Verifica se a busca de um evento por ID retorna o evento esperado.

testCreateEvent(): Testa a criação de um evento, simulando uma requisição POST e verificando se o evento criado é retornado corretamente com o status 201 Created.

testUpdateEvent(): Testa a atualização de um evento, simulando uma requisição PUT e verificando se o evento atualizado é retornado corretamente.

testDeleteEvent(): Verifica se a exclusão de um evento retorna o status 204 No Content.
*/
import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.dto.event.EventDto;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.model.Status;
import com.ufpb.sicred.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc

class EventControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private Event event;
    private EventDto eventDto;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setNome("Tech Conference");
        event.setDescricao("Annual technology conference");
        event.setDataInicio(LocalDateTime.of(2024, 11, 10, 9, 0)); // Adicionando horário 09:00
        event.setDataFim(LocalDateTime.of(2024, 11, 12, 18, 0));   // Adicionando horário 18:00
        event.setLocal("João Pessoa, PB");
        event.setMaxParticipantes(500);
        event.setStatus(Status.ATIVO);

        eventDto = new EventDto(
                event.getId(),
                event.getNome(),
                event.getDescricao(),
                event.getDataInicio(),  // Agora LocalDateTime
                event.getDataFim(),     // Agora LocalDateTime
                event.getLocal(),
                event.getMaxParticipantes(),
                event.getStatus(),
                event.getDataCriacao(),
                1L  // Organizador ID fictício
        );
    }

    @Test
    @WithMockUser(username = "organizador")
    void testListEvents() throws Exception {
        // Criar o organizador (User) simulado
        User organizador = new User();
        organizador.setId(1L);  // Defina o ID do organizador
        organizador.setNome("organizador");

        // Criar o evento simulado e associar o organizador
        Event event = new Event();
        event.setId(1L);
        event.setNome("Nome do Evento");
        event.setDescricao("Descrição do Evento");
        event.setOrganizador(organizador);  // Organizador não pode ser nulo

        // Simulando o comportamento do serviço para retornar uma lista de eventos
        Mockito.when(eventService.getAllEvents()).thenReturn(Arrays.asList(event));

        // Executando a requisição e verificando o resultado
        mockMvc.perform(get("/api/evento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(event.getId()))
                .andExpect(jsonPath("$[0].nome").value(event.getNome()))
                .andExpect(jsonPath("$[0].descricao").value(event.getDescricao()))
                .andExpect(jsonPath("$[0].organizadorId").value(organizador.getId()));  // Verificando o organizador
    }


    @Test
    @WithMockUser(username = "organizador")
    void testGetEvent() throws Exception {
        // Criar o organizador (User) simulado
        User organizador = new User();
        organizador.setId(1L);  // Defina o ID do organizador
        organizador.setNome("organizador");

        // Criar o evento simulado e associar o organizador
        Event event = new Event();
        event.setId(1L);
        event.setNome("Nome do Evento");
        event.setDescricao("Descrição do Evento");
        event.setOrganizador(organizador);  // Organizador não pode ser nulo

        // Simulando o comportamento do serviço para retornar o evento simulado
        Mockito.when(eventService.getEventById(1L)).thenReturn(event);

        // Executando a requisição e verificando o resultado
        mockMvc.perform(get("/api/evento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(event.getId()))
                .andExpect(jsonPath("$.nome").value(event.getNome()))
                .andExpect(jsonPath("$.descricao").value(event.getDescricao()))
                .andExpect(jsonPath("$.organizadorId").value(organizador.getId()));  // Verificando o organizador
    }


    @Test
    @WithMockUser(username = "organizador")
    void testCreateEvent() throws Exception {
        // Criar um usuário mockado
        User organizador = new User();
        organizador.setId(1L); // ID do organizador
        organizador.setNome("Organizador Teste");
        organizador.setEmail("organizador@teste.com");

        // Criar o evento mockado
        Event event = new Event();
        event.setId(1L);
        event.setNome("Evento Simples");
        event.setDescricao("Descrição do Evento Simples");
        event.setOrganizador(organizador); // Definindo o organizador

        // Mock do serviço
        Mockito.when(eventService.createEvent(Mockito.any(Event.class), Mockito.eq(1L))).thenReturn(event);

        // Configuração do DTO de forma simplificada
        String requestBody = "{ \"nome\": \"Evento Simples\", \"descricao\": \"Descrição do Evento Simples\" }";

        // Execução do teste
        mockMvc.perform(post("/api/evento/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        // Verificação da interação com o serviço
        Mockito.verify(eventService, Mockito.times(1)).createEvent(Mockito.any(Event.class), Mockito.eq(1L));
    }


    @Test
    @WithMockUser(username = "organizador")
    void testUpdateEvent() throws Exception {
        // Criar um usuário mockado como organizador
        User organizador = new User();
        organizador.setId(1L); // ID do organizador
        organizador.setNome("Organizador Teste");
        organizador.setEmail("organizador@teste.com");

        // Criar o evento mockado
        Event event = new Event();
        event.setId(1L);
        event.setNome("Evento Atualizado");
        event.setDescricao("Descrição do Evento Atualizado");
        event.setOrganizador(organizador); // Definindo o organizador

        // Criar o DTO correspondente ao evento
        EventDto eventDto = new EventDto();
        eventDto.setNome(event.getNome());
        eventDto.setDescricao(event.getDescricao());

        // Mock do serviço para atualização do evento
        Mockito.when(eventService.updateEvent(Mockito.eq(1L), Mockito.eq(1L), Mockito.any(Event.class)))
                .thenReturn(event);

        // Execução do teste
        mockMvc.perform(put("/api/evento/user/1/evento/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(eventDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(event.getId()))
                .andExpect(jsonPath("$.nome").value(event.getNome()))
                .andExpect(jsonPath("$.descricao").value(event.getDescricao()));
    }

    @Test
    @WithMockUser(username = "organizador")
    void testDeleteEvent() throws Exception {
        Mockito.doNothing().when(eventService).deleteEvent(1L, 1L);

        mockMvc.perform(delete("/api/evento/user/1/evento/1"))
                .andExpect(status().isNoContent());
    }
}