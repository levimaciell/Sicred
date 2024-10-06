package com.ufpb.sicred.entities;

import com.ufpb.sicred.dto.user.UserDto;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    private Tipo_usuario tipoUsuario;

    public User(Long id, String nome, String email, String senha, Tipo_usuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public User() {

    }

    public User(UserDto dto) {
        this.nome = dto.getNome();
        this.email = dto.getEmail();
        this.senha = dto.getSenha();
        this.tipoUsuario = Tipo_usuario.USUARIO;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Tipo_usuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Tipo_usuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
