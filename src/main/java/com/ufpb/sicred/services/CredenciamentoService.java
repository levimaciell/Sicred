package com.ufpb.sicred.services;

import com.ufpb.sicred.dto.credenciamento.CredenciamentoDto;
import com.ufpb.sicred.entities.Credenciamento;
import com.ufpb.sicred.entities.Inscricao;
import com.ufpb.sicred.exceptions.InscricaoNotFoundException;
import com.ufpb.sicred.model.StatusCredenciamento;
import com.ufpb.sicred.repositories.CredenciamentoRepository;
import com.ufpb.sicred.repositories.InscricaoRepository;
import com.ufpb.sicred.utils.Mapper;
import org.springframework.stereotype.Service;

@Service
public class CredenciamentoService {

    private CredenciamentoRepository credenciamentoRepository;
    private InscricaoRepository inscricaoRepository;

    public CredenciamentoService(CredenciamentoRepository credenciamentoRepository, InscricaoRepository inscricaoRepository) {
        this.credenciamentoRepository = credenciamentoRepository;
        this.inscricaoRepository = inscricaoRepository;
    }

    public CredenciamentoDto credenciar(Long idInscricao){
        Inscricao inscricao = inscricaoRepository.findById(idInscricao)
                .orElseThrow(() -> new InscricaoNotFoundException("Inscricao não encontrada"));

        Credenciamento credenciamento = credenciamentoRepository.findByInscricao(inscricao);
        credenciamento.setStatus(StatusCredenciamento.CREDENCIADO);

        credenciamentoRepository.save(credenciamento);

        return Mapper.convertToDto(credenciamento, CredenciamentoDto.class);
    }


    public CredenciamentoDto listarCredenciamento(Long idInscricao) {
        Inscricao inscricao = inscricaoRepository.findById(idInscricao)
                .orElseThrow(() -> new InscricaoNotFoundException("Inscricao não encontrada"));

        Credenciamento credenciamento = credenciamentoRepository.findByInscricao(inscricao);

        return Mapper.convertToDto(credenciamento, CredenciamentoDto.class);
    }
}
