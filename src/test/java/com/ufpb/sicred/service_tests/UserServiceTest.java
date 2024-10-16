package com.ufpb.sicred.service_tests;

import com.ufpb.sicred.dto.user.UserDto;
import com.ufpb.sicred.dto.user.UserListDto;
import com.ufpb.sicred.entities.Tipo_usuario;
import com.ufpb.sicred.entities.User;
import com.ufpb.sicred.exceptions.UserNotFoundException;
import com.ufpb.sicred.repositories.UserRepository;
import com.ufpb.sicred.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        service = new UserService(repository);

    }

    @Test
    void testCreateUser(){

        //Arrange

        UserDto creationDto = new UserDto();
        creationDto.setNome("TESTE");
        creationDto.setSenha("TESTE123");
        creationDto.setEmail("TESTE@gmail.com");

        User user = new User(creationDto);

        when(repository.save(any(User.class))).thenReturn(user);

        //Act
        service.createUser(creationDto);

        //Assert
        //Verificando se save é chamado mais de uma vez
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser(){
        //Act
        service.deleteUser(1L);

        //Assert
        //Verificando se delete é chamado mais de uma vez
        verify(repository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void testListUser_userExists(){
        //Arrange
        Long userId = 1L;
        User user = new User(1L, "user1", "user1@gmail.com", "user1", Tipo_usuario.USUARIO);
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        //Act
        UserListDto listDto = service.listUser(userId);

        //Assert
        //Verificando se save é chamado mais de uma vez
        assertNotNull(listDto);
        assertEquals(user.getId(), listDto.getId());
        assertEquals(user.getEmail(), listDto.getEmail());

    }

    @Test
    void testListUser_userNotFound(){
        //Arrange
        Long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(UserNotFoundException.class, () -> service.listUser(userId));
    }

    @Test
    void testListAllUsers_withUsers(){
        //Arrange
        User u1 = new User(1L, "user1", "user1@gmail.com", "user1", Tipo_usuario.USUARIO);
        User u2 = new User(2L, "user2", "user2@gmail.com", "user2", Tipo_usuario.USUARIO);
        when(repository.findAll()).thenReturn(Arrays.asList(u1, u2));

        //Act

        List<UserListDto> dtos = service.listAll();

        //Assert

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    @Test
    void testListAllUsers_withoutUsers(){
        //Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        //Act
        List<UserListDto> dtos = service.listAll();

        //Assert

        assertNotNull(dtos);
        assertEquals(0, dtos.size());
    }

    void testUpdateUser_userFound(){
        //Arrange
        Long userId = 1L;
        User user = new User(userId, "user1", "user1@gmail.com", "user1", Tipo_usuario.USUARIO);

        UserDto updateDto = new UserDto("UpdatedUser", "Updated@gmail.com", "Updated");
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        //Act
        UserListDto dto = service.updateUser(userId, new UserDto());

        //Assert
        assertNotNull(dto);
        assertEquals(userId, dto.getId());
        assertNotEquals(user.getNome(), dto.getNome());
        assertNotEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void testUpdateUser_userNotFound(){
        //Arrange
        Long userId = 1L;
        UserDto dto = new UserDto("user", "user@user.com", "user");
        when(repository.findById(userId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(UserNotFoundException.class, () -> service.updateUser(userId, dto));
    }
}
