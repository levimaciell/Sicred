package com.ufpb.sicred.services;

import com.ufpb.sicred.dto.user.UserDto;
import com.ufpb.sicred.dto.user.UserListDto;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.exceptions.InvalidUserCreationException;
import com.ufpb.sicred.exceptions.UserNotFoundException;
import com.ufpb.sicred.repositories.UserRepository;
import com.ufpb.sicred.utils.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

<<<<<<< HEAD

    public Long createUser(UserDto dto) {
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        User user = new User(dto);

        // Salva o usuário e retorna o ID gerado
        user = repository.save(user);
        return user.getId(); // Retorna o ID do usuário criado
=======
    public void createUser(UserDto dto){

        if(repository.findByNome(dto.getNome()).isPresent())
            throw new InvalidUserCreationException("Nome de usuário inválido");

        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        User user = new User(dto);

        repository.save(user);
>>>>>>> c0c95ae8a258cfa2c0c2ab7f2989ea6d3301ed1e
    }


    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public UserListDto listUser(Long id){
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return Mapper.convertToDto(user, UserListDto.class);
    }

    public List<UserListDto> listAll() {
        return repository.findAll().stream()
                .map(u -> Mapper.convertToDto(u, UserListDto.class))
                .toList();
    }


    public UserListDto updateUser(Long id, UserDto dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());

        repository.save(user);

        return Mapper.convertToDto(user, UserListDto.class);
    }
}
