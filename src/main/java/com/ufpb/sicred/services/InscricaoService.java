package com.ufpb.sicred.services;

import com.ufpb.sicred.dto.inscricao.InscricaoDto;
import com.ufpb.sicred.entities.Credenciamento;
import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.entities.Inscricao;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.exceptions.EventNotFoundException;
import com.ufpb.sicred.exceptions.InscricaoNotFoundException;
import com.ufpb.sicred.exceptions.UserNotFoundException;
import com.ufpb.sicred.model.StatusCredenciamento;
import com.ufpb.sicred.model.StatusInscricao;
import com.ufpb.sicred.repositories.CredenciamentoRepository;
import com.ufpb.sicred.repositories.EventRepository;
import com.ufpb.sicred.repositories.InscricaoRepository;
import com.ufpb.sicred.repositories.UserRepository;
import com.ufpb.sicred.utils.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InscricaoService {

    private InscricaoRepository inscricaoRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private CredenciamentoRepository credenciamentoRepository;

    public InscricaoService(InscricaoRepository inscricaoRepository, UserRepository userRepository, EventRepository eventRepository, CredenciamentoRepository credenciamentoRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.credenciamentoRepository = credenciamentoRepository;
    }

    public InscricaoDto createInscricao(InscricaoDto dto){

        User user = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UserNotFoundException("Usuário com tal id não encontrado"));

        Event event = eventRepository.findById(dto.getEventoId())
                .orElseThrow(() -> new EventNotFoundException("Evento com tal id não encontrado"));

        Inscricao inscricao = new Inscricao();

        inscricao.setUsuario(user);
        inscricao.setEvento(event);
        inscricao.setStatus(StatusInscricao.ACEITA);

        inscricaoRepository.save(inscricao);

        Credenciamento credenciamento = new Credenciamento();
        credenciamento.setInscricao(inscricao);
        credenciamento.setStatus(StatusCredenciamento.NAO_CREDENCIADO);
        credenciamentoRepository.save(credenciamento);

        return Mapper.convertToDto(inscricao, InscricaoDto.class);
    }

//    public void deleteInscricao(Long id) {
//        Inscricao inscricao = inscricaoRepository.findById(id)
//                .orElseThrow(() -> new InscricaoNotFoundException("Inscrição não encontrada"));
//        inscricaoRepository.delete(inscricao);
//    }

    public void deleteInscricao(Long id) {
        // Verifica se a inscrição existe
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new InscricaoNotFoundException("Inscrição não encontrada"));

        // Remove todos os credenciamentos relacionados à inscrição
        credenciamentoRepository.deleteById(id);  // Método para deletar credenciamentos associados

        // Agora, remove a inscrição
        inscricaoRepository.delete(inscricao);
    }


    public InscricaoDto listInscricao(Long id){
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new InscricaoNotFoundException("Inscrição não encontrada"));

        return Mapper.convertToDto(inscricao, InscricaoDto.class);
    }

    public List<InscricaoDto> listAll() {
        return inscricaoRepository.findAll().stream()
                .map(i -> Mapper.convertToDto(i, InscricaoDto.class))
                .toList();
    }
}
