package com.ufpb.sicred.service_tests;

import com.ufpb.sicred.dto.inscricao.InscricaoDto;
import com.ufpb.sicred.entities.Credenciamento;
import com.ufpb.sicred.entities.Event;
import com.ufpb.sicred.entities.Inscricao;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.exceptions.InscricaoNotFoundException;
import com.ufpb.sicred.model.StatusCredenciamento;
import com.ufpb.sicred.model.StatusInscricao;
import com.ufpb.sicred.repositories.CredenciamentoRepository;
import com.ufpb.sicred.repositories.EventRepository;
import com.ufpb.sicred.repositories.InscricaoRepository;
import com.ufpb.sicred.repositories.UserRepository;
import com.ufpb.sicred.services.InscricaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscricaoServiceTest {

    @InjectMocks
    private InscricaoService inscricaoService;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CredenciamentoRepository credenciamentoRepository; // Adicionado mock

    private InscricaoDto inscricaoDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        inscricaoDto = new InscricaoDto();
        inscricaoDto.setUsuarioId(1L);
        inscricaoDto.setEventoId(1L);
        inscricaoDto.setStatus(StatusInscricao.ACEITA);
    }

    @Test
    void createInscricao_ShouldReturnInscricaoDto() {
        // Criando um usuário e evento simulados
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setId(1L);

        // Simulando o comportamento do repositório para retornar o usuário e evento
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Simulando o comportamento do repositório de inscrições
        Inscricao inscricao = new Inscricao();
        inscricao.setUsuario(user);
        inscricao.setEvento(event);
        inscricao.setStatus(StatusInscricao.ACEITA);
        when(inscricaoRepository.save(any())).thenReturn(inscricao); // Alterado para retornar a inscrição simulada

        // Simulando o comportamento do credenciamentoRepository
        Credenciamento credenciamento = new Credenciamento();
        credenciamento.setInscricao(inscricao);
        credenciamento.setStatus(StatusCredenciamento.NAO_CREDENCIADO);
        when(credenciamentoRepository.save(any())).thenReturn(credenciamento); // Adicionado para evitar NullPointerException

        // Executando o método a ser testado
        InscricaoDto createdInscricao = inscricaoService.createInscricao(inscricaoDto);

        // Asserções para verificar o resultado
        assertNotNull(createdInscricao);
        assertEquals(inscricaoDto.getUsuarioId(), createdInscricao.getUsuarioId());
        assertEquals(inscricaoDto.getEventoId(), createdInscricao.getEventoId());
        assertEquals(StatusInscricao.ACEITA, createdInscricao.getStatus());
        verify(inscricaoRepository, times(1)).save(any());
        verify(credenciamentoRepository, times(1)).save(any()); // Verifica se o método save foi chamado
    }


    @Test
    void listAll_ShouldReturnListOfInscricaoDto() {
        List<Inscricao> inscricoes = new ArrayList<>();
        Inscricao inscricao = new Inscricao();
        inscricao.setUsuario(new User());
        inscricao.setEvento(new Event());
        inscricoes.add(inscricao);
        when(inscricaoRepository.findAll()).thenReturn(inscricoes);

        List<InscricaoDto> result = inscricaoService.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        // Adapte para corresponder aos valores esperados
        verify(inscricaoRepository, times(1)).findAll();
    }

    @Test
    void deleteInscricao_ShouldCallRepositoryDelete() {
        Long idToDelete = 1L;
        doNothing().when(inscricaoRepository).deleteById(idToDelete);

        inscricaoService.deleteInscricao(idToDelete);

        verify(inscricaoRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void findInscricaoById_ShouldReturnInscricaoDto_WhenFound() {
        Long idToFind = 1L;
        Inscricao inscricao = new Inscricao();
        inscricao.setUsuario(new User());
        inscricao.setEvento(new Event());
        inscricao.setStatus(StatusInscricao.ACEITA);
        when(inscricaoRepository.findById(idToFind)).thenReturn(Optional.of(inscricao));

        InscricaoDto foundInscricao = inscricaoService.listInscricao(idToFind);

        assertNotNull(foundInscricao);
        // Aqui você deve adaptar para converter a Inscricao para InscricaoDto corretamente
        assertEquals(inscricao.getUsuario().getId(), foundInscricao.getUsuarioId());
        assertEquals(inscricao.getEvento().getId(), foundInscricao.getEventoId());
        assertEquals(inscricao.getStatus(), foundInscricao.getStatus());
        verify(inscricaoRepository, times(1)).findById(idToFind);
    }

    @Test
    void findInscricaoById_ShouldThrowInscricaoNotFoundException_WhenNotFound() {
        Long idToFind = 2L;
        when(inscricaoRepository.findById(idToFind)).thenReturn(Optional.empty());

        assertThrows(InscricaoNotFoundException.class, () -> {
            inscricaoService.listInscricao(idToFind);
        });

        verify(inscricaoRepository, times(1)).findById(idToFind);
    }

}
