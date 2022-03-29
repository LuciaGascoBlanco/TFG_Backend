package com.lucia.nft.services;

import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.lucia.nft.dto.SignUpDto;
import com.lucia.nft.dto.UserDto;
import com.lucia.nft.dto.UserSummaryDto;
import com.lucia.nft.entities.User;
import com.lucia.nft.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserSummaryDto> searchUsers(String term) {
        List<User> users = userRepository.search("%" + term + "%");
        List<UserSummaryDto> usersToBeReturned = new ArrayList<>();

        users.forEach(user -> usersToBeReturned.add(new UserSummaryDto(user.getId(), user.getFirstName(), user.getLastName())));

        return usersToBeReturned;
    }

    public UserDto signUp(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new RuntimeException("Login already exists");
        }

        User user = new User(null, userDto.getFirstName(), userDto.getLastName(), userDto.getLogin(), passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())), null, null, null, null, LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return new UserDto(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getLogin());
    }
}
