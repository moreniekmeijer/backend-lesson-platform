package nl.moreniekmeijer.lessonplatform.dtos;

public class UserResponseDto {

    private String username;
    private String email;

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
}

