package ru.antonsibgatulin.bankingservice.mapper;

import ru.antonsibgatulin.bankingservice.dto.user.response.UserDto;
import ru.antonsibgatulin.bankingservice.entity.user.User;

import java.util.List;

public interface UserMapper {

    UserDto fromUserToUserDto(User user);

    List<UserDto> fromUserListToUserDtoList(List<User> userDtos);
}
