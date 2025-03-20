package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.UserInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.UserMapper;
import nl.moreniekmeijer.lessonplatform.models.User;
import nl.moreniekmeijer.lessonplatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto addUser(UserInputDto userInputDto) {
        User user = UserMapper.toEntity(userInputDto);
        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDto(savedUser);
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> foundUsers = userRepository.findAll();
        return foundUsers.stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return UserMapper.toResponseDto(user);
    }

    public UserResponseDto updateUser(Long id, UserInputDto userInputDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        UserMapper.updateEntity(user, userInputDto);
        User updatedUser = userRepository.save(user);
        return UserMapper.toResponseDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}