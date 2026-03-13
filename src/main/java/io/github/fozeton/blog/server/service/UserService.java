package io.github.fozeton.blog.server.service;

import io.github.fozeton.blog.server.dto.UploadFileDto;
import io.github.fozeton.blog.server.dto.UserDto;
import io.github.fozeton.blog.server.entity.User;
import io.github.fozeton.blog.server.exceptions.UserAlreadyExistsException;
import io.github.fozeton.blog.server.exceptions.UserAuthenticationException;
import io.github.fozeton.blog.server.exceptions.UserAvatarFailedChangeException;
import io.github.fozeton.blog.server.exceptions.UserNotFoundException;
import io.github.fozeton.blog.server.repository.UserRepository;
import io.github.fozeton.blog.server.utils.SavedFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class UserService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final SavedFile savedFile = new SavedFile();
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(UserDto userDto) {
        if (userRepository.existsByUserName(userDto.getUserName()))
            throw new UserAlreadyExistsException("The user already exists");

        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setAvatarUrl("");
        user.setPassword(encoder.encode(userDto.getPassword()));

        userRepository.save(user);
    }

    public void login(UserDto userDto) {
        User user = userRepository.findByUserName(userDto.getUserName());
        if(user == null) throw new UserNotFoundException("User not found");

        if (!userRepository.existsByUserName(userDto.getUserName()))
            throw new UserAuthenticationException("Incorrect login or password");

        if (!encoder.matches(userDto.getPassword(), user.getPassword()))
            throw new UserAuthenticationException("Incorrect login or password");
    }

    public void setAvatar(UploadFileDto file) {
        try {
            User user = userRepository.findByUserName(file.getUserName());
            String savedAvatar = savedFile.saveFile(file.getImg(), "avatars");
            if (savedAvatar.isEmpty()) throw new UserAvatarFailedChangeException("Failed to change avatar");
            if(user == null) throw new UserNotFoundException("User not found");
            user.setAvatarUrl(savedAvatar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
