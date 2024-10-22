package com.ufpb.sicred.dto.inscricao;

import com.ufpb.sicred.model.StatusInscricao;
import jakarta.validation.constraints.NotNull;

public class InscricaoDto {

    private Long id;
    @NotNull(message = "Usuario ID não pode ser nulo")
    private Long usuarioId;

    @NotNull(message = "Evento ID não pode ser nulo")
    private Long eventoId;

    @NotNull(message = "Status não pode ser nulo")
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

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
