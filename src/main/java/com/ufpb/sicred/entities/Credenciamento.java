package com.ufpb.sicred.entities;

import com.ufpb.sicred.model.StatusCredenciamento;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_credenciamento")
public class Credenciamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Inscricao inscricao;

    @Enumerated(EnumType.STRING)
    private StatusCredenciamento status;

    public Credenciamento(Long id, Inscricao inscricao, StatusCredenciamento status) {
        this.id = id;
        this.inscricao = inscricao;
        this.status = status;
    }

    public Credenciamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }

    public StatusCredenciamento getStatus() {
        return status;
    }

    public void setStatus(StatusCredenciamento status) {
        this.status = status;
    }
}
