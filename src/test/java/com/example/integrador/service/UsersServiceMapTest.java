package com.example.integrador.service;

import com.example.integrador.dto.UserDto;
import com.example.integrador.entity.UserMongoEntity;
import com.example.integrador.exception.UserNotFoundException;
import com.example.integrador.repository.UserMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceMapTest {

    @Mock
    private UserMongoRepository userMongoRepository;

    @InjectMocks
    private UsersServiceMap usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAll() {
        UserMongoEntity user = new UserMongoEntity();
        user.setId("7");
        user.setName("CristianoRonaldo");
        user.setEmail("Cr7@example.com");

        when(userMongoRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = usersService.all();
        assertEquals(1, result.size());
        assertEquals("CristianoRonaldo", result.get(0).getName());
        assertEquals("Cr7@example.com", result.get(0).getEmail());
    }

    @Test
    void testFindById() {
        UserMongoEntity user = new UserMongoEntity();
        user.setId("7");
        user.setName("CristianoRonaldo");
        user.setEmail("Cr7@example.com");

        when(userMongoRepository.findById("7")).thenReturn(Optional.of(user));

        Optional<UserDto> result = usersService.findById("7");
        assertTrue(result.isPresent());
        assertEquals("CristianoRonaldo", result.get().getName());
        assertEquals("Cr7@example.com", result.get().getEmail());
        System.out.println(user.getName()+" Encontrado!");
    }

    @Test
    void testFindByIdNotFound() {
        when(userMongoRepository.findById("8")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.findById("8"));
        System.out.println("Error! datos no encontrados");

    }

    @Test
    void testSave() {
        UserDto userDto = new UserDto();
        userDto.setName("Tom");
        userDto.setEmail("castrol@example.com");

        UserMongoEntity user = new UserMongoEntity();
        user.setName("Tom");
        user.setEmail("castrol@example.com");

        when(userMongoRepository.save(any(UserMongoEntity.class))).thenReturn(user);

        UserDto result = usersService.save(userDto);
        assertEquals("Tom", result.getName());
        assertEquals("castrol@example.com", result.getEmail());
        System.out.println("Dato Guardado");
    }

    @Test
    void testUpdate() {
        UserDto userDto = new UserDto();
        userDto.setName("Tom");
        userDto.setEmail("castrol@example.com");

        UserMongoEntity user = new UserMongoEntity();
        user.setId("36");
        user.setName("Castrol");
        user.setEmail("Supra@example.com");

        when(userMongoRepository.findById("36")).thenReturn(Optional.of(user));
        when(userMongoRepository.save(any(UserMongoEntity.class))).thenReturn(user);

        UserDto result = usersService.update(userDto, "36");
        assertEquals("Tom", result.getName());
        assertEquals("castrol@example.com", result.getEmail());
        System.out.println("Cambios Hechos");
    }

    @Test
    void testDeleteById() {
        UserMongoEntity user = new UserMongoEntity();
        user.setId("777");
        user.setName("vegetta");
        user.setEmail("simetrico@example.com");
        when(userMongoRepository.findById("777")).thenReturn(Optional.of(user));
        usersService.deleteById("777");
        verify(userMongoRepository, times(1)).delete(user);
        System.out.println("Dato Eliminado");
    }
}
