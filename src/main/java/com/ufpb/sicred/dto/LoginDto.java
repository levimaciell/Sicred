package com.ufpb.sicred.dto;

public class LoginDto {
    private String usuario;
    private String senha;

    public LoginDto(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public LoginDto() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
