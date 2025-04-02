package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotNull;

public class UserInputDto {

    @NotNull(message = "Username is required")
    private String username;
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    public UserInputDto() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
