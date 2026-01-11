package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.Email;

public class UserUpdateDto {

    @Email
    private String email;

    private String newPassword;
    private String currentPassword;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
}
