package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.UserInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserResponseDto;
import nl.moreniekmeijer.lessonplatform.models.User;

public class UserMapper {

    public static User toEntity(UserInputDto dto) {
        User user = new User();
        updateEntity(user, dto);
        return user;
    }

    public static UserResponseDto toResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUsername(user.getUsername());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }

    public static void updateEntity(User user, UserInputDto dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
    }
}
