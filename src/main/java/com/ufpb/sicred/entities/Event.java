package com.ufpb.sicred.entities;

import com.ufpb.sicred.model.Status;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_evento")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String local; // Pode ser nulo para eventos online

    private int maxParticipantes;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime dataCriacao;

    // Relacionamento com User para representar o organizador do evento
    @ManyToOne
    @JoinColumn(name = "organizador_id", nullable = false)
    private User organizador;

    public Event() {
    }

    public Event(Long id, String nome, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim,
                 String local, int maxParticipantes, Status status, LocalDateTime dataCriacao, User organizador) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.local = local;
        this.maxParticipantes = maxParticipantes;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.organizador = organizador;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getMaxParticipantes() {
        return maxParticipantes;
    }

    public void setMaxParticipantes(int maxParticipantes) {
        this.maxParticipantes = maxParticipantes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public User getOrganizador() {
        return organizador;
    }

    public void setOrganizador(User organizador) {
        this.organizador = organizador;
    }
}