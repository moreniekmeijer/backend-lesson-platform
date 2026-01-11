package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotNull
    @Email(message = "Ongeldig e-mailadres")
    private String email;

    @NotNull(message = "Wachtwoord is verplicht")
    @Size(min = 8, message = "Wachtwoord moet minstens 8 tekens bevatten")
    private String password;

    @NotNull(message = "Invite code is verplicht")
    private String inviteCode;

    private String fullName;

    public @NotNull @Email(message = "Ongeldig e-mailadres") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @Email(message = "Ongeldig e-mailadres") String email) {
        this.email = email;
    }

    public @NotNull(message = "Wachtwoord is verplicht") @Size(min = 8, message = "Wachtwoord moet minstens 8 tekens bevatten") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Wachtwoord is verplicht") @Size(min = 8, message = "Wachtwoord moet minstens 8 tekens bevatten") String password) {
        this.password = password;
    }

    public @NotNull(message = "Invite code is verplicht") String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(@NotNull(message = "Invite code is verplicht") String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
