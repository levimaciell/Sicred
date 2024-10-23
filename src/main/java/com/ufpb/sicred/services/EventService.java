package com.ufpb.sicred.services;

import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.entities.User;
//import com.ufpb.sicred.model.TipoUsuario;
import com.ufpb.sicred.entities.Tipo_usuario;
import com.ufpb.sicred.exceptions.EventCreationException;
import com.ufpb.sicred.exceptions.EventNotFoundException;
import com.ufpb.sicred.exceptions.UserNotFoundException;
import com.ufpb.sicred.repositories.EventRepository; // O repositório correspondente
import com.ufpb.sicred.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private UserRepository userRepository;
    private EventRepository eventRepository;

    public EventService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    // Criar Evento
    public Event createEvent(Event e, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + userId + " não encontrado"));

        if (!user.getTipoUsuario().equals(Tipo_usuario.ORGANIZADOR)) {
            throw new EventCreationException("Apenas organizadores podem criar eventos.");
        }

        // Associa o organizador ao evento
        e.setOrganizador(user);
        return eventRepository.save(e);
    }

    // Deletar Evento (somente o organizador pode deletar)
    public void deleteEvent(Long eventId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + userId + " não encontrado"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));

        if (!event.getOrganizador().equals(user)) {
            throw new RuntimeException("Apenas o organizador do evento pode deletá-lo.");
        }

        eventRepository.deleteById(eventId);
    }

    // Atualizar Evento (somente o organizador pode atualizar)
    public Event updateEvent(Long eventId, Long userId, Event event) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + userId + " não encontrado"));

        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));

        if (!existingEvent.getOrganizador().equals(user)) {
            throw new EventCreationException("Apenas o organizador do evento pode atualizá-lo.");
        }

        // Atualiza os dados do evento
        existingEvent.setNome(event.getNome());
        existingEvent.setDescricao(event.getDescricao());
        existingEvent.setDataInicio(event.getDataInicio());
        existingEvent.setDataFim(event.getDataFim());
        existingEvent.setLocal(event.getLocal());
        existingEvent.setMaxParticipantes(event.getMaxParticipantes());
        existingEvent.setStatus(event.getStatus());

        return eventRepository.save(existingEvent);
    }

    // Listar Evento por ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));
    }

    // Listar todos Eventos
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}