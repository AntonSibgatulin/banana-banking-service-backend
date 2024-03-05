package ru.antonsibgatulin.bankingservice.config.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.antonsibgatulin.bankingservice.dto.user.response.EmailAddressDto;
import ru.antonsibgatulin.bankingservice.dto.user.response.PhoneNumberDto;
import ru.antonsibgatulin.bankingservice.entity.user.EmailAddress;
import ru.antonsibgatulin.bankingservice.entity.user.PhoneNumber;
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
        return new PhoneNumberMapper() {
            @Override
            public PhoneNumberDto phoneNumberToPhoneNumberDto(PhoneNumber phoneNumber) {
                return new PhoneNumberDto(phoneNumber.getId(), phoneNumber.getNumber());
            }

            @Override
            public List<PhoneNumberDto> phoneNumberListToPhoneNumberDtoList(List<PhoneNumber> phoneNumbers) {
                List<PhoneNumberDto> phoneNumberDtos = new ArrayList<>();
                for (PhoneNumber phoneNumber : phoneNumbers) {
                    phoneNumberDtos.add(phoneNumberToPhoneNumberDto(phoneNumber));
                }
                return phoneNumberDtos;

            }
        };
    }

    @Bean
    public EmailAddressMapper emailAddressMapper() {
        return new EmailAddressMapper() {
            @Override
            public EmailAddressDto emailAddressToEmailAddressDto(EmailAddress emailAddress) {
                return new EmailAddressDto(emailAddress.getId(), emailAddress.getAddress());
            }

            @Override
            public List<EmailAddressDto> emailAddressListToEmailAddressDtoList(List<EmailAddress> emailAddresses) {
                List<EmailAddressDto> emailAddressDtos = new ArrayList<>();
                for (EmailAddress emailAddress : emailAddresses) {
                    emailAddressDtos.add(emailAddressToEmailAddressDto(emailAddress));
                }
                return emailAddressDtos;
            }
        };
    }
}
