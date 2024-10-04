package com.ufpb.sicred.controllers;

import com.ufpb.sicred.dto.UserDto;
import com.ufpb.sicred.dto.UserListDto;
import com.ufpb.sicred.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/usuario")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public void createUser(@RequestBody UserDto dto){
        service.createUser(dto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable Long id){
        service.deleteUser(id);
    }

    @GetMapping(path = "/{id}")
    public UserListDto listUser(@PathVariable Long id){
        return service.listUser(id);
    }

    @GetMapping
    public List<UserListDto> listUsers(){
        return service.listAll();
    }

    @PutMapping(path = "/{id}")
    public UserListDto updateUser(@PathVariable Long id, @RequestBody UserDto dto){
        return service.updateUser(id, dto);
    }

}
