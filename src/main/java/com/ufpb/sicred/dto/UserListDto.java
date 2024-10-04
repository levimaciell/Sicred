package com.ufpb.sicred.dto;

import com.ufpb.sicred.entities.Tipo_usuario;

public class UserListDto {
    private Long id;

    private String nome;
    private String email;
    private Tipo_usuario tipoUsuario;

    public UserListDto(Long id, String nome, String email, Tipo_usuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public UserListDto() {
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Tipo_usuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Tipo_usuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
