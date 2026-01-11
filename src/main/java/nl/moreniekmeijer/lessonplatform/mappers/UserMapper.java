package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.UserRegistrationDto;
import nl.moreniekmeijer.lessonplatform.dtos.UserResponseDto;
import nl.moreniekmeijer.lessonplatform.models.User;
import nl.moreniekmeijer.lessonplatform.models.Authority;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(UserRegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static UserResponseDto toResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setEmail(user.getEmail());
        responseDto.setFullName(user.getFullName());

        Set<String> authorities = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet());

        responseDto.setAuthorities(authorities);
        return responseDto;
    }
}
