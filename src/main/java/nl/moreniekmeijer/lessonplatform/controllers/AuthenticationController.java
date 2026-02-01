package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.config.CustomUserDetails;
import nl.moreniekmeijer.lessonplatform.dtos.AuthenticationRequest;
import nl.moreniekmeijer.lessonplatform.dtos.AuthenticationResponse;
import nl.moreniekmeijer.lessonplatform.dtos.UserRegistrationDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserResponseDto;
import nl.moreniekmeijer.lessonplatform.service.CustomUserDetailsService;
import nl.moreniekmeijer.lessonplatform.service.UserService;
import nl.moreniekmeijer.lessonplatform.utils.JwtUtil;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Value("${app.cookie.secure:true}")
    private boolean secureCookie;

    @Value("${app.cookie.same-site:None}")
    private String sameSiteCookie;

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

//    TODO: make .secure use global variable
    private ResponseEntity<AuthenticationResponse> createAuthResponse(
            UserDetails userDetails,
            HttpServletResponse response) {

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite(sameSiteCookie)
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(new AuthenticationResponse(accessToken));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody UserRegistrationDto userInputDto,
            HttpServletResponse response) {

        UserResponseDto savedUser = userService.addUser(userInputDto);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            savedUser.getEmail(),
                            userInputDto.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());

            ResponseEntity<AuthenticationResponse> authResponse = createAuthResponse(userDetails, response);

            URI location = URIUtil.createResourceUriUser(savedUser.getId());
            return ResponseEntity
                    .created(location)
                    .header(HttpHeaders.SET_COOKIE, authResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE))
                    .body(authResponse.getBody());

        } catch (AuthenticationException e) {
            throw new RuntimeException("Auto-authentication failed after registration", e);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(
                    authenticationRequest.getEmail()
            );

            return createAuthResponse(userDetails, response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long userId = Long.valueOf(jwtUtil.extractSubject(refreshToken));
            UserDetails userDetails = userDetailsService.loadUserByUserId(userId);

            String newAccessToken = jwtUtil.generateAccessToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(newAccessToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(0)
                .sameSite(sameSiteCookie)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/authenticated")
    public ResponseEntity<CustomUserDetails> getAuthenticatedUser(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(user);
    }
}