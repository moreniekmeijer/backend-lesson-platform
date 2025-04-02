package nl.moreniekmeijer.lessonplatform.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.moreniekmeijer.lessonplatform.models.Authority;

import java.util.Set;

public class UserResponseDto {

    private String username;
    private String email;
    @JsonSerialize
    private Set<Authority> authorities;

    public UserResponseDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}

