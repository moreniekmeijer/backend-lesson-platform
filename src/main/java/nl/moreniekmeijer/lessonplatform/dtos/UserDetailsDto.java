package nl.moreniekmeijer.lessonplatform.dtos;

import nl.moreniekmeijer.lessonplatform.models.Authority;

import java.util.Set;

public class UserDetailsDto {
    private String email;
    private String password;
    private Set<String> authorities;

    public UserDetailsDto(String email, String password, Set<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Set<String> getAuthorities() { return authorities; }
}
