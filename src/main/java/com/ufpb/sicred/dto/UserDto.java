package com.ufpb.sicred.dto;

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


        public @NotBlank(message = "O nome não pode estar vazia")
        String getNome() {
                return nome;
        }

        public void setNome(@NotBlank(message = "O nome não pode estar vazia") String nome) {
                this.nome = nome;
        }

        public @Email(message = "Email inválido") String getEmail() {
                return email;
        }

        public void setEmail(@Email(message = "Email inválido") String email) {
                this.email = email;
        }

        public @Size(min = 8, message = "A senha deve ter no mínimo 8 letras") String getSenha() {
                return senha;
        }

        public void setSenha(@Size(min = 8, message = "A senha deve ter no mínimo 8 letras") String senha) {
                this.senha = senha;
        }

}
