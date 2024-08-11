package com.example.integrador.controller.users;

import com.example.integrador.dto.UserDto;
import com.example.integrador.exception.UserNotFoundException;
import com.example.integrador.service.UsersService;
import com.example.integrador.service.UsersServiceMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return usersService.all();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable String id) {
        return usersService.findById(id).orElse(null);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return usersService.save(userDto);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        return usersService.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        usersService.deleteById(id);
    }
}