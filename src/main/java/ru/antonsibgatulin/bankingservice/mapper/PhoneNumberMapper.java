package ru.antonsibgatulin.bankingservice.mapper;

import ru.antonsibgatulin.bankingservice.dto.user.response.PhoneNumberDto;
import ru.antonsibgatulin.bankingservice.entity.user.PhoneNumber;

import java.util.List;

public interface PhoneNumberMapper {
    PhoneNumberDto phoneNumberToPhoneNumberDto(PhoneNumber phoneNumber);
    List<PhoneNumberDto> phoneNumberListToPhoneNumberDtoList(List<PhoneNumber> phoneNumbers);
}
