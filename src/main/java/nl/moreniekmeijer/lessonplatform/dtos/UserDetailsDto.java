package nl.moreniekmeijer.lessonplatform.dtos;

import nl.moreniekmeijer.lessonplatform.models.Authority;

import java.util.Set;

public class UserDetailsDto {
    private String username;
    private String password;
    private Set<Authority> authorities;

    public UserDetailsDto(String username, String password, Set<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Set<Authority> getAuthorities() { return authorities; }
}
