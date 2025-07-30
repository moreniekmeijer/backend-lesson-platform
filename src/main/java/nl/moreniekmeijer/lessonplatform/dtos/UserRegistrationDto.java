package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotNull(message = "Username is required")
    @Size(min = 3, max = 20, message = "Gebruikersnaam moet tussen 3 en 20 tekens bevatten")
    @Pattern(
            regexp = "^[a-zA-Z][a-zA-Z0-9._]{2,19}$",
            message = "Gebruikersnaam moet beginnen met een letter en mag alleen letters, cijfers, punten of underscores bevatten"
    )
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
