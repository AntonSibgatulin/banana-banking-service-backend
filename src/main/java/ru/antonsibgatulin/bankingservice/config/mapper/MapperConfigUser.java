package ru.antonsibgatulin.bankingservice.config.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.antonsibgatulin.bankingservice.dto.user.response.EmailAddressDto;
import ru.antonsibgatulin.bankingservice.dto.user.response.PhoneNumberDto;
import ru.antonsibgatulin.bankingservice.entity.user.EmailAddress;
import ru.antonsibgatulin.bankingservice.entity.user.PhoneNumber;
import ru.antonsibgatulin.bankingservice.impl.EmailAddressImpl;
import ru.antonsibgatulin.bankingservice.impl.PhoneNumberImpl;
import ru.antonsibgatulin.bankingservice.impl.UserMapperImpl;
import ru.antonsibgatulin.bankingservice.mapper.EmailAddressMapper;
import ru.antonsibgatulin.bankingservice.mapper.PhoneNumberMapper;
import ru.antonsibgatulin.bankingservice.mapper.UserMapper;


import java.util.ArrayList;
import java.util.List;


@Configuration
public class MapperConfigUser {
    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }


    @Bean
    public PhoneNumberMapper phoneNumberMapper() {
        return new PhoneNumberImpl();
    }

    @Bean
    public EmailAddressMapper emailAddressMapper() {
        return new EmailAddressImpl();
    }
}
