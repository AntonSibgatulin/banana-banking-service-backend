package ru.antonsibgatulin.bankingservice.config.mapper;

import org.springframework.stereotype.Component;
import ru.antonsibgatulin.bankingservice.dto.user.response.UserDto;
import ru.antonsibgatulin.bankingservice.entity.user.User;
import ru.antonsibgatulin.bankingservice.mapper.UserMapper;


import java.util.ArrayList;
import java.util.List;


public class UserMapperImpl  {

    @Override
    public UserDto fromUserToUserDto(User user) {

        return new UserDto(

                user.getId(),

                user.getFirstname(),

                user.getLastname(),

                user.getSecondname(),

                user.getBirthDay(),

                user.getUsername(),

            null,

                null
        );
    }

    @Override
    public List<UserDto> fromUserListToUserDtoList(List<User> userDtos) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userDtos) {
            userDtoList.add(fromUserToUserDto(user));
        }
        return userDtoList;
    }

    public void run() {

    }

    public static void main(String[] args) {
        UserMapperImpl userMapper = new UserMapper();

    }
}
