package com.ufpb.sicred.services;

import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.repositories.EventRepository; // O repositório correspondente
import com.ufpb.sicred.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {


    UserRepository userRepository;
    private EventRepository eventRepository;

    // Criar Evento
    public Event createEvent(Event e, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + userId + " não encontrado"));

        e.setOrganizador(user);

        return eventRepository.save(e);
    }

    // Excluir Evento
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id); // Exclui o evento pelo ID
    }

    // Atualizar Evento
    public Event updateEvent(Long eventId, Event u) {
        Optional<Event> eventData = eventRepository.findById(eventId);
        if(eventData.isPresent()){
            Event toUpdate = eventData.get();
            toUpdate.setNome(u.getNome());
            return eventRepository.save(toUpdate);
        }
        return null;
    }

    // Listar Evento por ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado")); // Retorna o evento ou lança uma exceção
    }

    // Listar todos Eventos
    public List<Event> getAllEvents() {
        return eventRepository.findAll(); // Retorna todos os eventos ativos
    }
}