package ru.antonsibgatulin.bankingservice.mapper;

import ru.antonsibgatulin.bankingservice.dto.user.response.EmailAddressDto;
import ru.antonsibgatulin.bankingservice.entity.user.EmailAddress;

import java.util.List;

public interface EmailAddressMapper {
    EmailAddressDto emailAddressToEmailAddressDto(EmailAddress emailAddress);

    List<EmailAddressDto> emailAddressListToEmailAddressDtoList(List<EmailAddress> emailAddresses);

}
