package nl.moreniekmeijer.lessonplatform.service;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.*;
import nl.moreniekmeijer.lessonplatform.exceptions.InvalidInviteCodeException;
import nl.moreniekmeijer.lessonplatform.exceptions.UsernameAlreadyExistsException;
import nl.moreniekmeijer.lessonplatform.mappers.MaterialMapper;
import nl.moreniekmeijer.lessonplatform.mappers.UserMapper;
import nl.moreniekmeijer.lessonplatform.models.*;
import nl.moreniekmeijer.lessonplatform.repositories.MaterialRepository;
import nl.moreniekmeijer.lessonplatform.repositories.PasswordResetTokenRepository;
import nl.moreniekmeijer.lessonplatform.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MaterialRepository materialRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender mailSender;

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Value("${invite.code.guest}")
    private String guestInviteCode;

    @Value("${invite.code.group1}")
    private String group1InviteCode;

    @Value("${invite.code.group2}")
    private String group2InviteCode;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MaterialRepository materialRepository, PasswordResetTokenRepository passwordResetTokenRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.materialRepository = materialRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailSender = mailSender;
    }

    public UserResponseDto addUser(UserRegistrationDto userInputDto) {
        if (userRepository.existsById(userInputDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Deze gebruikersnaam is al in gebruik.");
        }

        UserRole role = getRoleFromInviteCode(userInputDto.getInviteCode());

        User user = UserMapper.toEntity(userInputDto);
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        User savedUser = userRepository.save(user);

        addAuthority(savedUser.getUsername(), "ROLE_" + role.name());

        return UserMapper.toResponseDto(savedUser);
    }

    private UserRole getRoleFromInviteCode(String inviteCode) {
        if (inviteCode.equals(guestInviteCode)) {
            return UserRole.GUEST;
        } else if (inviteCode.equals(group1InviteCode)) {
            return UserRole.GROUP_1;
        } else if (inviteCode.equals(group2InviteCode)) {
            return UserRole.GROUP_2;
        } else {
            throw new InvalidInviteCodeException("Ongeldige registratiecode");
        }
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

    @Transactional(readOnly = true)
    public List<MaterialResponseDto> getBookmarkedMaterials(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getBookmarkedMaterials().stream()
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

    @Transactional
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Geen gebruiker gevonden met dit e-mailadres."));

        // Verwijder eerdere tokens voor deze gebruiker
        passwordResetTokenRepository.deleteByUsername(user.getUsername());

        // Maak nieuw token aan
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken resetToken = new PasswordResetToken(token, user.getUsername(), expiryDate);
        passwordResetTokenRepository.save(resetToken);

        String resetLink = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Reset je wachtwoord");
        message.setText("Hallo " + user.getUsername() + ",\n\n"
                + "Je kunt je wachtwoord resetten via deze link:\n"
                + resetLink + "\n\n"
                + "Deze link is 15 minuten geldig.\n\n");

        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Ongeldig of verlopen token."));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Token is verlopen.");
        }

        User user = userRepository.findById(resetToken.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Verwijder token na gebruik
        passwordResetTokenRepository.delete(resetToken);
    }
}