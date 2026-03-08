package io.github.fozeton.blog.server.service;

import io.github.fozeton.blog.server.dto.UserDto;
import io.github.fozeton.blog.server.entity.User;
import io.github.fozeton.blog.server.exceptions.UserAlreadyExistsException;
import io.github.fozeton.blog.server.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void register(UserDto user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new UserAlreadyExistsException("The user already exists");
        }
        User entity = new User();
        entity.setUserName(user.getUserName());
        entity.setPassword(encoder.encode(user.getPassword()));
        entity.setDateTime(LocalDateTime.now());

        userRepository.save(entity);
    }

}
