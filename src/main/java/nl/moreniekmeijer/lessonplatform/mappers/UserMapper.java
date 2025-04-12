package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.UserRegistrationDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserResponseDto;
import nl.moreniekmeijer.lessonplatform.models.User;

public class UserMapper {

    public static User toEntity(UserRegistrationDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static UserResponseDto toResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUsername(user.getUsername());
        responseDto.setEmail(user.getEmail());
        responseDto.setAuthorities(user.getAuthorities());
        return responseDto;
    }
}
