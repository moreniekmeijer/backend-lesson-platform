package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.*;
import nl.moreniekmeijer.lessonplatform.exceptions.InvalidInviteCodeException;
import nl.moreniekmeijer.lessonplatform.exceptions.UsernameAlreadyExistsException;
import nl.moreniekmeijer.lessonplatform.mappers.MaterialMapper;
import nl.moreniekmeijer.lessonplatform.mappers.UserMapper;
import nl.moreniekmeijer.lessonplatform.models.Authority;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.User;
import nl.moreniekmeijer.lessonplatform.repositories.MaterialRepository;
import nl.moreniekmeijer.lessonplatform.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MaterialRepository materialRepository;

    @Value("${invite.code}")
    private String requiredInviteCode;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MaterialRepository materialRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.materialRepository = materialRepository;
    }

    public UserResponseDto addUser(UserRegistrationDto userInputDto) {
        if (userRepository.existsById(userInputDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Deze gebruikersnaam is al in gebruik.");
        }

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

    public UserResponseDto updateUser(String username, UserUpdateDto inputDto) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));

        user.setEmail(inputDto.getEmail());

        if (inputDto.getPassword() != null && !inputDto.getPassword().isBlank()) {
            if (inputDto.getCurrentPassword() == null || inputDto.getCurrentPassword().isBlank()) {
                throw new IllegalArgumentException("Huidig wachtwoord is vereist voor wijziging.");
            }

            if (!passwordEncoder.matches(inputDto.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Huidig wachtwoord is onjuist.");
            }

            user.setPassword(passwordEncoder.encode(inputDto.getPassword()));
        }

        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }


    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        userRepository.deleteById(username);
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        User user = userRepository.findById(username).get();
        UserResponseDto userResponseDto = UserMapper.toResponseDto(user);
        return userResponseDto.getAuthorities();
    }

    public void addAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }

    public void assignMaterialToUser(String username, Long materialId) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        user.addMaterial(material);
        userRepository.save(user);
    }

    public List<MaterialResponseDto> getSavedMaterials(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getSavedMaterials().stream()
                .map(MaterialMapper::toResponseDto)
                .toList();
    }

    public void removeMaterialFromUser(String username, Long materialId) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        user.removeMaterial(material);
        userRepository.save(user);
    }
}