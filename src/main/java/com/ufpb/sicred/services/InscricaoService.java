package com.ufpb.sicred.services;

import com.ufpb.sicred.repositories.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class InscricaoService {

    private UserRepository repository;

    public InscricaoService(UserRepository repository) {
        this.repository = repository;
    }

    public void createUser(){

    }

}
