package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.UserInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserResponseDto;
import nl.moreniekmeijer.lessonplatform.services.UserService;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
        // userService.addAuthority(savedUser, "ROLE_USER");

        URI location = URIUtil.createResourceUriUser(savedUser.getUsername());
        return ResponseEntity.created(location).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String username, @Valid @RequestBody UserInputDto userInputDto) {
        UserResponseDto updatedUser = userService.updateUser(username, userInputDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}

