package ru.antonsibgatulin.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.antonsibgatulin.bankingservice.entity.user.PhoneNumber;
import ru.antonsibgatulin.bankingservice.entity.user.User;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    Boolean existsByNumberAndUser(String number, User user);

    Boolean existsByNumber(String number);

    PhoneNumber getPhoneNumberByIdAndUser(Long id, User user);

    PhoneNumber getPhoneNumberByNumber(String number);
}

