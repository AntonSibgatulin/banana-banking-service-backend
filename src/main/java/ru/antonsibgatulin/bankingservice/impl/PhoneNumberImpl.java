package ru.antonsibgatulin.bankingservice.impl;

import ru.antonsibgatulin.bankingservice.dto.user.response.PhoneNumberDto;
import ru.antonsibgatulin.bankingservice.entity.user.PhoneNumber;
import ru.antonsibgatulin.bankingservice.mapper.PhoneNumberMapper;

import java.util.ArrayList;
import java.util.List;

public class PhoneNumberImpl implements PhoneNumberMapper {
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
}
