package com.ufpb.sicred.configuration;

import com.ufpb.sicred.entities.Tipo_usuario;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrganizadorUserSeeder {

    private UserRepository repository;

    public OrganizadorUserSeeder(UserRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seedOrganizador(){
        if (repository.findByNome("organizador").isEmpty()) {
            User organizador = new User();
            organizador.setNome("organizador");
            organizador.setEmail("organizador@gmail.com");
            organizador.setSenha("organizador123");
            organizador.setTipoUsuario(Tipo_usuario.ORGANIZADOR);
            repository.save(organizador);
        }
    }
}
