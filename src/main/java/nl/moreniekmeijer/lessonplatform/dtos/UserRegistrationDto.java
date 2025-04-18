package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotNull(message = "Username is required")
    private String username;

    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Wachtwoord moet minstens 8 tekens bevatten")
    private String password;

    @NotNull(message = "Invite code is required")
    private String inviteCode;

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

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
