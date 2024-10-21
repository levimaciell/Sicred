package com.ufpb.sicred.service_tests;
/*
* testCreateEvent_asOrganizer: Testa a criação de um evento com sucesso por um usuário com o papel de organizador.
testCreateEvent_asNonOrganizer: Verifica se uma exceção é lançada ao tentar criar um evento por um usuário que não é organizador.
testDeleteEvent_asOrganizer: Testa se o organizador consegue deletar um evento.
testDeleteEvent_asNonOrganizer: Verifica se um usuário que não é o organizador não pode deletar o evento.
testUpdateEvent_asOrganizer: Testa se o organizador pode atualizar um evento.
testGetEventById_eventExists: Testa se um evento existente é retornado corretamente.
testGetEventById_eventNotFound: Verifica se uma exceção é lançada quando o evento não é encontrado.
testGetAllEvents_withEvents: Verifica se todos os eventos são retornados quando há eventos.
testGetAllEvents_withoutEvents: Verifica se uma lista vazia é retornada quando não há eventos.
*
*
* */

import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.entities.Tipo_usuario;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.repositories.EventRepository;
import com.ufpb.sicred.repositories.UserRepository;
import com.ufpb.sicred.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class EventServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventService = new EventService(userRepository, eventRepository);
    }

    @Test
    void testCreateEvent_asOrganizer() {
        // Arrange
        Long userId = 1L;
        User organizer = new User();
        organizer.setId(userId);
        organizer.setTipoUsuario(Tipo_usuario.ORGANIZADOR);

        Event event = new Event();
        event.setNome("Test Event");

        when(userRepository.findById(userId)).thenReturn(Optional.of(organizer));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // Act
        Event createdEvent = eventService.createEvent(event, userId);

        // Assert
        assertNotNull(createdEvent);
        assertEquals(event.getNome(), createdEvent.getNome());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testCreateEvent_asNonOrganizer() {
        // Arrange
        Long userId = 1L;
        User nonOrganizer = new User();
        nonOrganizer.setId(userId);
        nonOrganizer.setTipoUsuario(Tipo_usuario.USUARIO);

        Event event = new Event();
        event.setNome("Test Event");

        when(userRepository.findById(userId)).thenReturn(Optional.of(nonOrganizer));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventService.createEvent(event, userId));
        assertEquals("Apenas organizadores podem criar eventos.", exception.getMessage());
    }

    @Test
    void testDeleteEvent_asOrganizer() {
        // Arrange
        Long userId = 1L;
        Long eventId = 1L;

        User organizer = new User();
        organizer.setId(userId);
        organizer.setTipoUsuario(Tipo_usuario.ORGANIZADOR);

        Event event = new Event();
        event.setId(eventId);
        event.setOrganizador(organizer);

        when(userRepository.findById(userId)).thenReturn(Optional.of(organizer));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        eventService.deleteEvent(eventId, userId);

        // Assert
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void testDeleteEvent_asNonOrganizer() {
        Long userId = 1L;
        Long eventId = 1L;

        User nonOrganizer = new User();
        nonOrganizer.setId(userId);
        nonOrganizer.setTipoUsuario(Tipo_usuario.USUARIO);

        User organizer = new User();
        organizer.setId(2L);

        Event event = new Event();
        event.setId(eventId);
        event.setOrganizador(organizer);

        when(userRepository.findById(userId)).thenReturn(Optional.of(nonOrganizer));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventService.deleteEvent(eventId, userId));
        assertEquals("Apenas o organizador do evento pode deletá-lo.", exception.getMessage());
    }

    @Test
    void testUpdateEvent_asOrganizer() {
        Long userId = 1L;
        Long eventId = 1L;

        User organizer = new User();
        organizer.setId(userId);
        organizer.setTipoUsuario(Tipo_usuario.ORGANIZADOR);

        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        existingEvent.setOrganizador(organizer);

        Event updatedEvent = new Event();
        updatedEvent.setNome("Updated Event");

        when(userRepository.findById(userId)).thenReturn(Optional.of(organizer));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(existingEvent);

        // Act
        Event result = eventService.updateEvent(eventId, userId, updatedEvent);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Event", result.getNome());
        verify(eventRepository, times(1)).save(existingEvent);
    }

    @Test
    void testGetEventById_eventExists() {
        // Arrange
        Long eventId = 1L;
        Event event = new Event();
        event.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        Event foundEvent = eventService.getEventById(eventId);

        // Assert
        assertNotNull(foundEvent);
        assertEquals(eventId, foundEvent.getId());
    }

    @Test
    void testGetEventById_eventNotFound() {
        // Arrange
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventService.getEventById(eventId));
        assertEquals("Evento não encontrado", exception.getMessage());
    }

    @Test
    void testGetAllEvents_withEvents() {

        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(2L);

        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        // Act
        List<Event> events = eventService.getAllEvents();

        // Assert
        assertNotNull(events);
        assertEquals(2, events.size());
    }

    @Test
    void testGetAllEvents_withoutEvents() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        List<Event> events = eventService.getAllEvents();

        assertNotNull(events);
        assertEquals(0, events.size());
    }
}