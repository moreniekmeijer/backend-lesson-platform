package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.UserDetailsDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserResponseDto;
import nl.moreniekmeijer.lessonplatform.exceptions.InvalidInviteCodeException;
import nl.moreniekmeijer.lessonplatform.mappers.UserMapper;
import nl.moreniekmeijer.lessonplatform.models.Authority;
import nl.moreniekmeijer.lessonplatform.models.User;
import nl.moreniekmeijer.lessonplatform.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${invite.code}")
    private String requiredInviteCode;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Moet anders
    public UserResponseDto addUser(UserInputDto userInputDto) {
        if (!requiredInviteCode.equals(userInputDto.getInviteCode())) {
            throw new InvalidInviteCodeException("Ongeldige registratiecode");
        }

        User user = UserMapper.toEntity(userInputDto);
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDto(savedUser);
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> foundUsers = userRepository.findAll();
        return foundUsers.stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }

    public UserResponseDto getUser(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return UserMapper.toResponseDto(user);
    }

    public UserDetailsDto getUserWithPassword(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        return new UserDetailsDto(user.getUsername(), user.getPassword(), user.getAuthorities());
    }


    public UserResponseDto updateUser(String username, UserInputDto userInputDto) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        UserMapper.updateEntity(user, userInputDto);
        User updatedUser = userRepository.save(user);
        return UserMapper.toResponseDto(updatedUser);
    }

    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        userRepository.deleteById(username);
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new EntityNotFoundException("User not found with username: " + username);
        User user = userRepository.findById(username).get();
        UserResponseDto userResponseDto = UserMapper.toResponseDto(user);
        return userResponseDto.getAuthorities();
    }

    public void addAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new EntityNotFoundException("User not found with username: " + username);
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new EntityNotFoundException("User not found with username: " + username);
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }
}