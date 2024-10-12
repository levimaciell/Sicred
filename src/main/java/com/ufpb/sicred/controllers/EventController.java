package com.ufpb.sicred.controllers;

import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.dto.EventDto; // Supondo que você tenha criado um DTO para Event
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

    // Criar Evento
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventDto eventDto) {
        Event createdEvent = eventService.createEvent(eventDto);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    // Excluir Evento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    // Atualizar Evento
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto) {
        Event updatedEvent = eventService.updateEvent(id, eventDto);
        return ResponseEntity.ok(updatedEvent); // Retorna 200 OK com o evento atualizado
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
}
