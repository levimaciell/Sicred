package com.ufpb.sicred.controllers;

import com.ufpb.sicred.dto.user.UserDto;
import com.ufpb.sicred.dto.user.UserListDto;
import com.ufpb.sicred.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/usuario")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

//    @PostMapping
//    public ResponseEntity createUser(@RequestBody @Valid UserDto dto, HttpServletRequest request){
//
//        service.createUser(dto);
//        return ResponseEntity.created(URI.create(request.getRequestURI())).build();
//    }
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDto dto, HttpServletRequest request) {
        Long createdUserId = service.createUser(dto); // Chama o serviço e obtém o ID

        // Constrói a URI de localização
        URI location = URI.create(request.getRequestURI() + "/" + createdUserId);
        return ResponseEntity.created(location).build();
    }


//    @DeleteMapping(path = "/{id}")
//    public void deleteUser(@PathVariable Long id){
//        service.deleteUser(id);
//    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    return ResponseEntity.noContent().build(); // Retorna 204 No Content
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
    public UserListDto updateUser(@PathVariable Long id, @RequestBody @Valid UserDto dto){
        return service.updateUser(id, dto);
    }

}
