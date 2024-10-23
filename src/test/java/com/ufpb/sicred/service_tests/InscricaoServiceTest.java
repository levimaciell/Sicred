package com.ufpb.sicred.service_tests;

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

        // Mock para retornar uma inscrição ao procurar pelo ID
        Inscricao mockInscricao = new Inscricao();
        mockInscricao.setId(idToDelete);

        when(inscricaoRepository.findById(idToDelete)).thenReturn(Optional.of(mockInscricao));

        // Chamar o método que está sendo testado
        inscricaoService.deleteInscricao(idToDelete);

        // Verificar se delete foi chamado uma vez com a inscrição mockada
        verify(inscricaoRepository, times(1)).delete(mockInscricao);
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


    @Test
    void createInscricao_ShouldThrowUsuarioNotFoundException_WhenUsuarioDoesNotExist() {
        // Simulando que o usuário não existe
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Espera que uma exceção seja lançada
        assertThrows(UserNotFoundException.class, () -> {
            inscricaoService.createInscricao(inscricaoDto);
        });

        verify(inscricaoRepository, never()).save(any());
        verify(credenciamentoRepository, never()).save(any());
    }

    @Test
    void createInscricao_ShouldThrowEventoNotFoundException_WhenEventoDoesNotExist() {
        // Simulando que o evento não existe
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // Espera que uma exceção seja lançada
        assertThrows(EventNotFoundException.class, () -> {
            inscricaoService.createInscricao(inscricaoDto);
        });

        verify(inscricaoRepository, never()).save(any());
        verify(credenciamentoRepository, never()).save(any());
    }


}
