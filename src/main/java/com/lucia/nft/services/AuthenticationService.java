package com.lucia.nft.services;

import java.nio.CharBuffer;

import javax.transaction.Transactional;
import java.util.Optional;
import java.time.LocalDateTime;

import com.lucia.nft.dto.CredentialsDto;
import com.lucia.nft.dto.UserDto;
import com.lucia.nft.entities.User;
import com.lucia.nft.repositories.UserRepository;
import com.lucia.nft.dto.SignUpDto;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto signUp(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        User user = new User(null, userDto.getFirstName(), userDto.getLastName(), userDto.getLogin(), passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())), null, null, null, LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return new UserDto(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getLogin());
    }

    //"authenticate" usado por "validateCredentials" || "findByLogin" usado por "validateToken"

    @Transactional
    public UserDto authenticate(CredentialsDto credentialsDto) {          
        User user = userRepository.findByLogin(credentialsDto.getLogin()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {  //true if the raw password, after encoding, matches the encoded password from storage
            return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin());
        }
        throw new RuntimeException("Contraseña incorrecta");
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("Token no encontrado"));
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin());
    }
}
