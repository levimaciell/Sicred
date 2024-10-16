package com.ufpb.sicred.services;

import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.model.TipoUsuario;
import com.ufpb.sicred.repositories.EventRepository; // O repositório correspondente
import com.ufpb.sicred.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {


    UserRepository userRepository;
    private EventRepository eventRepository;

    public Event createEvent(Event e, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + userId + " não encontrado"));

        if (!user.getTipoUsuario().equals(TipoUsuario.ORGANIZADOR)) {
            throw new RuntimeException("Apenas organizadores podem criar eventos.");
        }

        // Associa o organizador ao evento
        e.setOrganizador(user);
        return eventRepository.save(e);
    }


    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public Event updateEvent(Long eventId, Event event) {
        Optional<Event> eventData = eventRepository.findById(eventId);
        if(eventData.isPresent()){
            Event toUpdate = eventData.get();
            toUpdate.setNome(event.getNome());
            return eventRepository.save(toUpdate);
        }
        return null;
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }

    // Listar todos Eventos
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}