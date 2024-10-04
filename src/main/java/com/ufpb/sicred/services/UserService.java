package com.ufpb.sicred.services;

import com.ufpb.sicred.dto.UserDto;
import com.ufpb.sicred.dto.UserListDto;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.repositories.UserRepository;
import com.ufpb.sicred.utils.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    public void createUser(UserDto dto){
        User user = new User(dto);

        repository.save(user);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public UserListDto listUser(Long id){
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("usuario nao encontrado"));

        return Mapper.convertToDto(user, UserListDto.class);
    }

    public List<UserListDto> listAll() {
        return repository.findAll().stream()
                .map(u -> Mapper.convertToDto(u, UserListDto.class))
                .toList();
    }


    public UserListDto updateUser(Long id, UserDto dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("usuario não encontrado"));

        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());

        repository.save(user);

        return Mapper.convertToDto(user, UserListDto.class);
    }
}
