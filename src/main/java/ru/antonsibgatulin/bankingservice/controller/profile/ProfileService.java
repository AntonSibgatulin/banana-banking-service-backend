package ru.antonsibgatulin.bankingservice.controller.profile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.antonsibgatulin.bankingservice.dto.user.request.*;
import ru.antonsibgatulin.bankingservice.entity.user.EmailAddress;
import ru.antonsibgatulin.bankingservice.entity.user.PhoneNumber;
import ru.antonsibgatulin.bankingservice.entity.user.User;
import ru.antonsibgatulin.bankingservice.except.AlreadyExistException;
import ru.antonsibgatulin.bankingservice.except.BadRequestException;
import ru.antonsibgatulin.bankingservice.except.NotFoundException;
import ru.antonsibgatulin.bankingservice.repository.EmailAddressRepository;
import ru.antonsibgatulin.bankingservice.repository.PhoneNumberRepository;
import ru.antonsibgatulin.bankingservice.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);


    private final UserRepository userRepository;
    private final EmailAddressRepository emailAddressRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    public void updateProfile(Authentication authentication,  UserSetUpProfileDto userDto) {
        logger.info("Updating profile for user: {}", authentication.getName());

        // Получение текущего пользователя из аутентификации
        User currentUser = userRepository.getUserByUsername(authentication.getName());


        // Обновление данных пользователя
        currentUser.setFirstname(userDto.getFirstname());
        currentUser.setLastname(userDto.getLastname());
        currentUser.setSecondname(userDto.getSecondname());
        currentUser.setBirthDay(userDto.getBirthDay());

        // Сохранение обновленного пользователя в репозитории
        userRepository.save(currentUser);
        logger.info("Profile updated for user: {}", authentication.getName());



    }


    public void addPhoneNumber(Authentication authentication, ProfileAddPhoneDto profileAddPhoneDto) {
        logger.info("Adding phone number for user: {}", authentication.getName());

        // Get the current user from the authentication object
        User currentUser = userRepository.getUserByUsername(authentication.getName());

        if (phoneNumberRepository.existsByNumber(profileAddPhoneDto.getPhone())) {
            throw new AlreadyExistException("Phone number already use");
        }

        // Create a new PhoneNumber entity and add it to the user's list
        try {
            PhoneNumber phoneNumberEntity = new PhoneNumber();
            phoneNumberEntity.setNumber(profileAddPhoneDto.getPhone());
            phoneNumberEntity.setUser(currentUser);
            currentUser.getPhoneNumbers().add(phoneNumberEntity);

            // Save the updated user to the database
            userRepository.save(currentUser);


        } catch (Exception e) {
            e.printStackTrace();
            throw new AlreadyExistException("Phone number already use");
        }
        logger.info("Phone number added for user: {}", authentication.getName());

    }


    public void addEmailAddress(Authentication authentication, ProfileAddEmailDto profileAddEmailDto) {
        logger.info("Adding email address for user: {}", authentication.getName());

        // Get the current user from the authentication object

        User currentUser = userRepository.getUserByUsername(authentication.getName());


        if (emailAddressRepository.existsByAddress(profileAddEmailDto.getEmail())) {
            throw new AlreadyExistException("Email already use");
        }

        // Create a new EmailAddress entity and add it to the user's list
        try {
            EmailAddress emailAddressEntity = new EmailAddress();
            emailAddressEntity.setAddress(profileAddEmailDto.getEmail());
            emailAddressEntity.setUser(currentUser);
            currentUser.getEmailAddresses().add(emailAddressEntity);


            // Save the updated user to the database

            userRepository.save(currentUser);


        } catch (Exception e) {
            throw new AlreadyExistException("Email already use");
        }
        logger.info("Email address added for user: {}", authentication.getName());

    }


    public void deletePhone(Authentication authentication, Long phoneId) {
        logger.info("Deleting phone for user: {}", authentication.getName());

        User currentUser = userRepository.getUserByUsername(authentication.getName());

        var phoneNumber = phoneNumberRepository.getPhoneNumberByIdAndUser(phoneId, currentUser);
        if (phoneNumber != null) {
            if (currentUser.getPhoneNumbers().size() == 1) {
                throw new BadRequestException("You cannot delete last phone number");
            }
            phoneNumberRepository.delete(phoneNumber);
        } else {
            throw new NotFoundException("Phone number not found");
        }
        logger.info("Phone deleted for user: {}", authentication.getName());



    }


    public void deleteEmail(Authentication authentication, Long emailId) {
        logger.info("Deleting email for user: {}", authentication.getName());

        User currentUser = userRepository.getUserByUsername(authentication.getName());

        var emailAddress = emailAddressRepository.getEmailAddressByIdAndUser(emailId, currentUser);
        if (emailAddress != null) {
            if (currentUser.getPhoneNumbers().size() == 1) {
                throw new BadRequestException("You cannot delete last email");
            }
            emailAddressRepository.delete(emailAddress);


        } else {
            throw new NotFoundException("Email not found");
        }
        logger.info("Email deleted for user: {}", authentication.getName());

    }


    public void changePhoneNumber(Authentication authentication, Long phoneId, ProfileChangePhoneDto profileChangePhoneDto) {
        logger.info("Changing phone for user: {}", authentication.getName());

        User currentUser = userRepository.getUserByUsername(authentication.getName());

        var phoneNumber = phoneNumberRepository.getPhoneNumberByIdAndUser(phoneId, currentUser);
        if (phoneNumber != null) {

            if (phoneNumberRepository.existsByNumber(profileChangePhoneDto.getPhone())) {
                throw new AlreadyExistException("New phone number already exist");
            }

            try {
                PhoneNumber phoneNumberEntity = new PhoneNumber();
                phoneNumberEntity.setNumber(profileChangePhoneDto.getPhone());
                phoneNumberEntity.setUser(currentUser);
                currentUser.getPhoneNumbers().add(phoneNumberEntity);

                // Save the updated user to the database
                userRepository.save(currentUser);
            } catch (Exception e) {
                throw new AlreadyExistException("Phone number already use");
            }


            phoneNumberRepository.delete(phoneNumber);
        } else {
            throw new NotFoundException("Phone number not found to change");
        }
        logger.info("Phone changed for user: {}", authentication.getName());


    }


    public void changeEmailAddress(Authentication authentication, Long emailId, ProfileChangeEmailDto profileChangeEmailDto) {
        logger.info("Changing email for user: {}", authentication.getName());

        User currentUser = userRepository.getUserByUsername(authentication.getName());

        var emailAddress = emailAddressRepository.getEmailAddressByIdAndUser(emailId, currentUser);
        if (emailAddress != null) {

            if (emailAddressRepository.existsByAddress(profileChangeEmailDto.getEmail())) {
                throw new AlreadyExistException("New email already exist");
            }
            try {
                EmailAddress emailAddressEntity = new EmailAddress();
                emailAddressEntity.setAddress(profileChangeEmailDto.getEmail());
                emailAddressEntity.setUser(currentUser);
                currentUser.getEmailAddresses().add(emailAddressEntity);


                // Save the updated user to the database

                userRepository.save(currentUser);
            } catch (Exception e) {
                throw new AlreadyExistException("Email already use");
            }


            emailAddressRepository.delete(emailAddress);
        } else {
            throw new NotFoundException("Email not found to change");
        }
        logger.info("Email changed for user: {}", authentication.getName());


    }

}
