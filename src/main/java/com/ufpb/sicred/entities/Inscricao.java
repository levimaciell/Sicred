package com.ufpb.sicred.entities;

import com.ufpb.sicred.model.StatusInscricao;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_inscricao")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User usuario;

    @ManyToOne
    private Event evento;

    @Enumerated(value = EnumType.STRING)
    private StatusInscricao status;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Credenciamento credenciamento;

    public Inscricao(Long id, User usuario, Event evento, StatusInscricao status, Credenciamento credenciamento) {
        this.id = id;
        this.usuario = usuario;
        this.evento = evento;
        this.status = status;
        this.credenciamento = credenciamento;
    }

    public Inscricao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Event getEvento() {
        return evento;
    }

    public void setEvento(Event evento) {
        this.evento = evento;
    }

    public StatusInscricao getStatus() {
        return status;
    }

    public void setStatus(StatusInscricao status) {
        this.status = status;
    }

    public Credenciamento getCredenciamento() {
        return credenciamento;
    }

    public void setCredenciamento(Credenciamento credenciamento) {
        this.credenciamento = credenciamento;
    }
}
