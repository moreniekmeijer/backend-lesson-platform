package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.config.CustomUserDetails;
import nl.moreniekmeijer.lessonplatform.dtos.*;
import nl.moreniekmeijer.lessonplatform.service.UserService;
import nl.moreniekmeijer.lessonplatform.utils.JwtUtil;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody UserRegistrationDto userInputDto) {
        UserResponseDto savedUser = userService.addUser(userInputDto);

        URI location = URIUtil.createResourceUriUser(savedUser.getId());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.created(location).body(new AuthenticationResponse(jwt));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("#user.id == #id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("#user.id == #id")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN') or #user.id == #id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/authorities")
    public ResponseEntity<Set<String>> getUserAuthorities(
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getAuthorities(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/authorities")
    public ResponseEntity<Void> addUserAuthority(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        String authority = (String) body.get("authority");
        userService.addAuthority(id, authority);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/authorities/{authority}")
    public ResponseEntity<Void> removeUserAuthority(
            @PathVariable Long id,
            @PathVariable String authority) {
        userService.removeAuthority(id, authority);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#user.id == #id")
    @PutMapping("/{id}/materials/{materialId}")
    public ResponseEntity<Void> assignMaterialToUser(
            @PathVariable Long id,
            @PathVariable Long materialId,
            @AuthenticationPrincipal CustomUserDetails user) {
        userService.assignMaterialToUser(id, materialId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#user.id == #id")
    @GetMapping("/{id}/materials")
    public ResponseEntity<List<MaterialResponseDto>> getBookmarkedMaterials(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(userService.getBookmarkedMaterials(id));
    }

    @PreAuthorize("#user.id == #id")
    @DeleteMapping("/{id}/materials/{materialId}")
    public ResponseEntity<Void> removeMaterialFromUser(
            @PathVariable Long id,
            @PathVariable Long materialId,
            @AuthenticationPrincipal CustomUserDetails user) {
        userService.removeMaterialFromUser(id, materialId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        userService.createPasswordResetToken(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }
}

