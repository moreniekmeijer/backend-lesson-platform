package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.AuthenticationResponse;
import nl.moreniekmeijer.lessonplatform.dtos.UserRegistrationDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserResponseDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserUpdateDto;
import nl.moreniekmeijer.lessonplatform.services.UserService;
import nl.moreniekmeijer.lessonplatform.utils.JwtUtil;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import nl.moreniekmeijer.lessonplatform.exceptions.BadRequestException;
import org.springframework.context.annotation.Profile;
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
        userService.addAuthority(savedUser.getUsername(), "ROLE_USER");

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

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
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
            return ResponseEntity.noContent().build();
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
}

