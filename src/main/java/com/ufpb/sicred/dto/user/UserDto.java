package com.ufpb.sicred.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto{

        @NotBlank(message = "O nome não pode estar vazia")
        private String nome;

        @NotBlank
        @Email(message = "Email inválido")
        private String email;

        @Size(min = 8, message = "A senha deve ter no mínimo 8 letras")
        private String senha;

        public UserDto(String nome, String email, String senha) {
                this.nome = nome;
                this.email = email;
                this.senha = senha;
        }

        public UserDto() {
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

        public void setSenha(String senha) { this.senha = senha; }

}
