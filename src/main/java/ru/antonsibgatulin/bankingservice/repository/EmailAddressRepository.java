package ru.antonsibgatulin.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.antonsibgatulin.bankingservice.entity.user.EmailAddress;
import ru.antonsibgatulin.bankingservice.entity.user.User;

public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {
    Boolean existsByAddressAndUser(String address, User user);

    Boolean existsByAddress(String address);

    EmailAddress getEmailAddressByIdAndUser(Long id, User user);
}
