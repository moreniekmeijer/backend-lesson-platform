package nl.moreniekmeijer.lessonplatform.service;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.*;
import nl.moreniekmeijer.lessonplatform.exceptions.InvalidInviteCodeException;
import nl.moreniekmeijer.lessonplatform.mappers.MaterialMapper;
import nl.moreniekmeijer.lessonplatform.mappers.UserMapper;
import nl.moreniekmeijer.lessonplatform.models.*;
import nl.moreniekmeijer.lessonplatform.repositories.MaterialRepository;
import nl.moreniekmeijer.lessonplatform.repositories.PasswordResetTokenRepository;
import nl.moreniekmeijer.lessonplatform.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

        if (userRepository.existsByEmail(userInputDto.getEmail())) {
            throw new IllegalArgumentException("Dit e-mailadres is al in gebruik.");
        }

        UserRole role = getRoleFromInviteCode(userInputDto.getInviteCode());

        User user = UserMapper.toEntity(userInputDto);
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.addAuthority("ROLE_" + role.name());

        User savedUser = userRepository.save(user);
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

    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden: " + userId));
        return UserMapper.toResponseDto(user);
    }

    public User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gebruiker niet gevonden: " + userId
                ));
    }

    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gebruiker niet gevonden: " + email
                ));
    }

    public UserResponseDto updateUser(Long userId, UserUpdateDto inputDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden: " + userId));

        if (!user.getEmail().equals(inputDto.getEmail()) &&
                userRepository.existsByEmail(inputDto.getEmail())) {
            throw new IllegalArgumentException("Dit e-mailadres is al in gebruik.");
        }
        user.setEmail(inputDto.getEmail());

        if (inputDto.getNewPassword() != null && !inputDto.getNewPassword().isBlank()) {
            if (inputDto.getCurrentPassword() == null || inputDto.getCurrentPassword().isBlank()) {
                throw new IllegalArgumentException("Huidig wachtwoord is vereist voor wijziging.");
            }

            if (!passwordEncoder.matches(inputDto.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Huidig wachtwoord is onjuist.");
            }

            user.setPassword(passwordEncoder.encode(inputDto.getNewPassword()));
        }

        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden"));
        userRepository.delete(user);
    }

    public Set<String> getAuthorities(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden: " + userId));

        return user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet());
    }

    public void addAuthority(Long userId, String authority) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden: " + userId));
        user.addAuthority(authority);
        userRepository.save(user);
    }

    public void removeAuthority(Long userId, String authority) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden: " + userId));

        user.getAuthorities().stream()
                .filter(a -> a.getAuthority().equalsIgnoreCase(authority))
                .findFirst()
                .ifPresentOrElse(
                        a -> {
                            user.removeAuthority(a);
                            userRepository.save(user);
                        },
                        () -> { throw new EntityNotFoundException("Authority niet gevonden: " + authority); }
                );
    }

    @Transactional
    public void assignMaterialToUser(Long userId, Long materialId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden"));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new EntityNotFoundException("Material not found"));

        user.addMaterial(material);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponseDto> getBookmarkedMaterials(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden"));

        return user.getBookmarkedMaterials().stream()
                .map(MaterialMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public void removeMaterialFromUser(Long userId, Long materialId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Gebruiker niet gevonden"));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new EntityNotFoundException("Material not found"));

        user.removeMaterial(material);
        userRepository.save(user);
    }

    @Transactional
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Geen gebruiker gevonden met dit e-mailadres."));

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);
        passwordResetTokenRepository.save(resetToken);

        String resetLink = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Reset je wachtwoord");
        message.setText("Hallo " + user.getFullName() + ",\n\n"
                + "Je kunt je wachtwoord resetten via deze link:\n"
                + resetLink + "\n\n"
                + "Deze link is 15 minuten geldig.\n\n");

        mailSender.send(message);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Ongeldig of verlopen token."));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Token is verlopen.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.deleteByUser(user);
    }
}