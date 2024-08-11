package com.example.integrador.service;

import com.example.integrador.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    UserDto save(UserDto user);
    Optional<UserDto> findById(String id);
    List<UserDto> all();
    void deleteById(String id);
    UserDto update(UserDto user, String userId);
}
