package com.ufpb.sicred.controllers;

import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.dto.event.EventDto; // Supondo que você tenha criado um DTO para Event
import com.ufpb.sicred.services.EventService; // A classe de serviço correspondente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evento")
public class EventController {

    @Autowired
    private EventService eventService;

    // Criar Evento (somente organizador)
    @PostMapping("/user/{userId}")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDto, @PathVariable Long userId) {
        Event event = convertToEntity(eventDto);
        Event createdEvent = eventService.createEvent(event, userId);
        return new ResponseEntity<>(convertToDTO(createdEvent), HttpStatus.CREATED);
    }

    // Excluir Evento (somente organizador)
    @DeleteMapping("/user/{userId}/evento/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, @PathVariable Long userId) {
        eventService.deleteEvent(id, userId);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    // Atualizar Evento (somente organizador)
    @PutMapping("/user/{userId}/evento/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @PathVariable Long userId, @RequestBody EventDto eventDto) {
        Event event = convertToEntity(eventDto);
        Event updatedEvent = eventService.updateEvent(id, userId, event);
        return ResponseEntity.ok(convertToDTO(updatedEvent)); // Retorna 200 OK com o evento atualizado
    }

    // Listar Evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(event); // Retorna 200 OK com os detalhes do evento
    }

    // Listar todos Eventos
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events); // Retorna 200 OK com a lista de eventos
    }

    // Métodos de conversão entre DTO e Entidade
    private EventDto convertToDTO(Event event) {
        return new EventDto(
                event.getId(),
                event.getNome(),
                event.getDescricao(),
                event.getDataInicio(),
                event.getDataFim(),
                event.getLocal(),
                event.getMaxParticipantes(),
                event.getStatus(),
                event.getDataCriacao(),
                event.getOrganizador().getId() // Organizador é o User
        );
    }

    private Event convertToEntity(EventDto eventDto) {
        Event event = new Event();
        event.setNome(eventDto.getNome());
        event.setDescricao(eventDto.getDescricao());
        event.setDataInicio(eventDto.getDataInicio());
        event.setDataFim(eventDto.getDataFim());
        event.setLocal(eventDto.getLocal());
        event.setMaxParticipantes(eventDto.getMaxParticipantes());
        event.setStatus(eventDto.getStatus());
        event.setDataCriacao(eventDto.getDataCriacao());
        return event;
    }
}