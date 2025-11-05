package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.*;
import nl.moreniekmeijer.lessonplatform.service.UserService;
import nl.moreniekmeijer.lessonplatform.utils.JwtUtil;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import nl.moreniekmeijer.lessonplatform.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

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

        URI location = URIUtil.createResourceUriUser(savedUser.getUsername());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.created(location).body(new AuthenticationResponse(jwt));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PreAuthorize("#username == authentication.name")
    @PutMapping("/{username}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String username, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        UserResponseDto updatedUser = userService.updateUser(username, userUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getAuthorities(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> authorities) {
        try {
            String authorityName = (String) authorities.get("authority");
            userService.addAuthority(username, authorityName);
            return ResponseEntity.ok("Authority added to user: " + username);
        }
        catch (Exception ex) {
            throw new BadRequestException("Could not add authority to user");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) {
        userService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#username == authentication.name")
    @PutMapping("/{username}/materials/{materialId}")
    public ResponseEntity<Void> assignMaterialToUser(
            @PathVariable String username,
            @PathVariable Long materialId) {
        userService.assignMaterialToUser(username, materialId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#username == authentication.name")
    @GetMapping("/{username}/materials")
    public ResponseEntity<List<MaterialResponseDto>> getBookmarkedMaterials(@PathVariable String username) {
        List<MaterialResponseDto> bookmarkedMaterials = userService.getBookmarkedMaterials(username);
        return ResponseEntity.ok(bookmarkedMaterials);
    }

    @PreAuthorize("#username == authentication.name")
    @DeleteMapping("/{username}/materials/{materialId}")
    public ResponseEntity<Void> removeMaterialFromUser(
            @PathVariable String username,
            @PathVariable Long materialId) {
        userService.removeMaterialFromUser(username, materialId);
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

