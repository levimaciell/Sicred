package com.ufpb.sicred.dto.credenciamento;

import com.ufpb.sicred.dto.inscricao.InscricaoDto;
import com.ufpb.sicred.model.StatusCredenciamento;

public class CredenciamentoDto {

    private Long id;
    private InscricaoDto inscricao;
    private StatusCredenciamento status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InscricaoDto getInscricao() {
        return inscricao;
    }

    public void setInscricao(InscricaoDto inscricao) {
        this.inscricao = inscricao;
    }

    public StatusCredenciamento getStatus() {
        return status;
    }

    public void setStatus(StatusCredenciamento status) {
        this.status = status;
    }
}
