package nl.moreniekmeijer.lessonplatform.controllers;

import nl.moreniekmeijer.lessonplatform.dtos.AuthenticationRequest;
import nl.moreniekmeijer.lessonplatform.dtos.AuthenticationResponse;
import nl.moreniekmeijer.lessonplatform.utils.JwtUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AuthenticationController {

    public AuthenticationManager authenticationManager;
    public UserDetailsService userDetailsService;
    public JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/authenticated")
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
        System.out.println(authentication);
        System.out.println(principal);

        return ResponseEntity.ok().body(principal);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (BadCredentialsException e) {
            throw new Exception("Invalid username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}