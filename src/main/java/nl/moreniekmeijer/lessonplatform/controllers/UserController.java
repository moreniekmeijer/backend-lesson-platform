package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.UserInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserResponseDto;
import nl.moreniekmeijer.lessonplatform.services.UserService;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import nl.moreniekmeijer.lessonplatform.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody UserInputDto userInputDto) {
        UserResponseDto savedUser = userService.addUser(userInputDto);
        userService.addAuthority(savedUser.getUsername(), "ROLE_USER");

        URI location = URIUtil.createResourceUriUser(savedUser.getUsername());
        return ResponseEntity.created(location).body(savedUser);
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
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String username, @Valid @RequestBody UserInputDto userInputDto) {
        UserResponseDto updatedUser = userService.updateUser(username, userInputDto);
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

