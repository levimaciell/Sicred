package com.ufpb.sicred.dto.inscricao;

import com.ufpb.sicred.model.StatusInscricao;

public class InscricaoDto {

    private Long usuarioId;
    private Long eventoId;
    private StatusInscricao status;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public StatusInscricao getStatus() {
        return status;
    }

    public void setStatus(StatusInscricao status) {
        this.status = status;
    }
}
