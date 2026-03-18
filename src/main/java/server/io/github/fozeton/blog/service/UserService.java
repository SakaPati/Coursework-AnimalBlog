package server.io.github.fozeton.blog.service;

import server.io.github.fozeton.blog.dto.UploadFileDto;
import server.io.github.fozeton.blog.dto.UserDto;
import server.io.github.fozeton.blog.entity.User;
import server.io.github.fozeton.blog.exceptions.UserAlreadyExistsException;
import server.io.github.fozeton.blog.exceptions.UserAuthenticationException;
import server.io.github.fozeton.blog.exceptions.UserAvatarFailedChangeException;
import server.io.github.fozeton.blog.exceptions.UserNotFoundException;
import server.io.github.fozeton.blog.repository.UserRepository;
import server.io.github.fozeton.blog.utils.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class UserService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final FileUtils fileUtils = new FileUtils();
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(UserDto userDto) {
        if (userRepository.existsByUserName(userDto.getUserName()))
            throw new UserAlreadyExistsException("The user already exists");

        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setAvatarUrl("defaultAccountAvatar.png");
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
            String savedAvatar = fileUtils.saveFile(file.getImg(), "avatars");
            if (savedAvatar.isEmpty()) throw new UserAvatarFailedChangeException("Failed to change avatar");
            if(user == null) throw new UserNotFoundException("User not found");
            user.setAvatarUrl(savedAvatar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<byte[]> getAvatar(String userName) {
        String path = userRepository.findUserAvatarUrlByUserName(userName);
        try{
            Path imgPath = Paths.get("images/avatars/" + path);
            return fileUtils.getResponseEntity(imgPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
