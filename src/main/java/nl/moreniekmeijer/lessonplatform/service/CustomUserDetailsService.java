package nl.moreniekmeijer.lessonplatform.service;

import nl.moreniekmeijer.lessonplatform.config.CustomUserDetails;
import nl.moreniekmeijer.lessonplatform.dtos.UserDetailsDto;
import nl.moreniekmeijer.lessonplatform.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userService.getUserEntityByEmail(email);
        return toUserDetails(user);
    }

    public UserDetails loadUserByUserId(Long id) {
        User user = userService.getUserEntityById(id);
        return toUserDetails(user);
    }

    private UserDetails toUserDetails(User user) {
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities().stream()
                        .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                        .toList()
        );
    }
}
