package com.lucia.nft.services;

import java.nio.CharBuffer;
import javax.transaction.Transactional;

import com.lucia.nft.dto.CredentialsDto;
import com.lucia.nft.dto.UserDto;
import com.lucia.nft.entities.User;
import com.lucia.nft.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto authenticate(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.getLogin()).orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin());
        }
        throw new RuntimeException("Contraseña incorrecta");
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("Token not found"));
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin());
    }
}
