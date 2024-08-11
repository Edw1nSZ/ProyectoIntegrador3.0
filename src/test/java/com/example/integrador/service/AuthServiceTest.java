package com.example.integrador.service;

import com.example.integrador.config.JwtService;
import com.example.integrador.dto.auth.AuthDto;
import com.example.integrador.dto.auth.LoginDto;
import com.example.integrador.dto.auth.RegisterDto;
import com.example.integrador.entity.UserMongoEntity;
import com.example.integrador.exception.CustomAuthenticationException; // Importar la excepción personalizada
import com.example.integrador.repository.UserMongoRepository;
import com.example.integrador.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserMongoRepository userMongoRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        LoginDto loginDto = new LoginDto("Rexplay@example.com", "relay");
        UserMongoEntity user = new UserMongoEntity();
        user.setEmail("Rexplay@example.com");
        user.setPassword("hashedPassword");

        when(userMongoRepository.findByEmail(loginDto.getUsername())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(jwtService.getToken(user)).thenReturn("jwtToken");

        AuthDto result = authService.login(loginDto);

        assertNotNull(result);
        assertEquals("jwtToken", result.getToken());
    }

    @Test
    void testLoginUserNotFound() {
        LoginDto loginDto = new LoginDto("Rexplay@example.com", "relay");

        when(userMongoRepository.findByEmail(loginDto.getUsername())).thenReturn(Optional.empty());

        AuthDto result = authService.login(loginDto);

        assertNull(result);
    }

    @Test
    void testLoginAuthenticationError() {
        LoginDto loginDto = new LoginDto("Rexplay@example.com", "relay");
        UserMongoEntity user = new UserMongoEntity();
        user.setEmail("ds@example.com");

        when(userMongoRepository.findByEmail(loginDto.getUsername())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new CustomAuthenticationException("Error de autenticación"));

        assertThrows(RuntimeException.class, () -> authService.login(loginDto));
        System.out.println("Error de autenticación");
    }

    @Test
    void testRegister() {
        RegisterDto registerDto = new RegisterDto("Armando Paredes", "castillo@example.com", "password");
        UserMongoEntity user = new UserMongoEntity();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword("hashedPassword");

        // Configuración de los mocks
        when(userMongoRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("hashedPassword");
        when(userMongoRepository.save(any(UserMongoEntity.class))).thenReturn(user);
        when(jwtService.getToken(argThat(u -> u instanceof UserMongoEntity && ((UserMongoEntity) u).getEmail().equals(user.getEmail())))).thenReturn("jwtToken");

        // Llamada al método que se está probando
        AuthDto result = authService.register(registerDto);

        // Verificación del resultado
        assertNotNull(result);
        assertEquals("jwtToken", result.getToken());
    }


    @Test
    void testRegisterUserAlreadyExists() {
        RegisterDto registerDto = new RegisterDto("Rxplayer", "Steve@example.com", "password");

        when(userMongoRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.of(new UserMongoEntity()));

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerDto));
    }

    @Test
    void testRegisterInvalidPassword() {
        RegisterDto registerDto = new RegisterDto("Edwin Cruz", "SteveSZ@example.com", "");

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerDto));
    }
}
