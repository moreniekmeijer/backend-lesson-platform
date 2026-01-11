package nl.moreniekmeijer.lessonplatform.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.moreniekmeijer.lessonplatform.models.Authority;

import java.util.Set;

public class UserResponseDto {

    private Long id;
    private String email;
    private String fullName;
    @JsonSerialize
    private Set<String> authorities;

    public UserResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}

