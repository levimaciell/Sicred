package com.ufpb.sicred.services;

import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.dto.EventDto; // Supondo que você tenha criado um DTO para Event
import com.ufpb.sicred.repositories.EventRepository; // O repositório correspondente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Criar Evento
    public Event createEvent(EventDto eventDto) {
        Event event = new Event();
        event.setNome(eventDto.getNome());
        event.setDescricao(eventDto.getDescricao());
        event.setDataInicio(eventDto.getDataInicio());
        event.setDataFim(eventDto.getDataFim());
        event.setLocal(eventDto.getLocal());
        event.setTipoUsuario(eventDto.getTipoUsuario());
        event.setMaxParticipantes(eventDto.getMaxParticipantes());
        event.setStatus(eventDto.getStatus());
        event.setDataCriacao(LocalDateTime.now()); // Define a data de criação como agora
        return eventRepository.save(event); // Salva o evento no banco de dados
    }

    // Excluir Evento
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id); // Exclui o evento pelo ID
    }

    // Atualizar Evento
    public Event updateEvent(Long id, EventDto eventDto) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setNome(eventDto.getNome());
            event.setDescricao(eventDto.getDescricao());
            event.setDataInicio(eventDto.getDataInicio());
            event.setDataFim(eventDto.getDataFim());
            event.setLocal(eventDto.getLocal());
            event.setTipoUsuario(eventDto.getTipoUsuario());
            event.setMaxParticipantes(eventDto.getMaxParticipantes());
            event.setStatus(eventDto.getStatus());
            return eventRepository.save(event); // Salva as atualizações no banco de dados
        } else {
            throw new RuntimeException("Evento não encontrado"); // Exceção personalizada ou use uma mais específica
        }
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