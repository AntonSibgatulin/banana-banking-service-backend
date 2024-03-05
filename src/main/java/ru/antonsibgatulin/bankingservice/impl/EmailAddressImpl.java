package ru.antonsibgatulin.bankingservice.impl;

import ru.antonsibgatulin.bankingservice.dto.user.response.EmailAddressDto;
import ru.antonsibgatulin.bankingservice.entity.user.EmailAddress;
import ru.antonsibgatulin.bankingservice.mapper.EmailAddressMapper;

import java.util.ArrayList;
import java.util.List;

public class EmailAddressImpl implements EmailAddressMapper {
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
}
