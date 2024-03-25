package ru.antonsibgatulin.bankingservice.controller.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.antonsibgatulin.bankingservice.dto.user.request.UserAuthenticationDto;
import ru.antonsibgatulin.bankingservice.dto.user.request.UserRegistrationDto;
import ru.antonsibgatulin.bankingservice.dto.user.response.UserAuthDto;
import ru.antonsibgatulin.bankingservice.entity.user.EmailAddress;
import ru.antonsibgatulin.bankingservice.entity.user.PhoneNumber;
import ru.antonsibgatulin.bankingservice.entity.user.User;
import ru.antonsibgatulin.bankingservice.entity.wallet.Wallet;
import ru.antonsibgatulin.bankingservice.except.AuthenticationException;
import ru.antonsibgatulin.bankingservice.except.RegistrationException;
import ru.antonsibgatulin.bankingservice.repository.EmailAddressRepository;
import ru.antonsibgatulin.bankingservice.repository.PhoneNumberRepository;
import ru.antonsibgatulin.bankingservice.repository.UserRepository;
import ru.antonsibgatulin.bankingservice.repository.WalletRepository;
import ru.antonsibgatulin.bankingservice.service.jwt.JwtService;

import javax.validation.constraints.Email;


@RequiredArgsConstructor
@Component
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    private final EmailAddressRepository emailAddressRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    private final JwtService jwtService;


    public UserAuthDto signIn(UserAuthenticationDto userAuthenticationDto) {

        logger.info("Starting user authentication process with login: {}", userAuthenticationDto.getLogin());

        var user = userRepository.getUserByUsernameAndPassword(userAuthenticationDto.getLogin(), userAuthenticationDto.getPassword());

        if (user == null) {

            logger.error("User authentication failed for login: {}", userAuthenticationDto.getLogin());

            throw new AuthenticationException("Authorization failed");

        }

        logger.info("User authentication successful for login: {}", userAuthenticationDto.getLogin());

        return getUserAuthDto(user);

    }


    @Transactional
    public UserAuthDto singUp(UserRegistrationDto userRegistrationDto) {

        logger.info("Starting user registration process with login: {}", userRegistrationDto.getLogin());

        if (userRepository.existsByUsername(userRegistrationDto.getLogin())) {

            logger.error("User registration failed for login: {}", userRegistrationDto.getLogin());

            throw new RegistrationException("Login is already in use");

        }

        if (emailAddressRepository.existsByAddress(userRegistrationDto.getEmail())) {

            logger.error("User registration failed for email: {}", userRegistrationDto.getEmail());

            throw new RegistrationException("Email is already in use");

        }

        if (phoneNumberRepository.existsByNumber(userRegistrationDto.getPhone())) {

            logger.error("User registration failed for phone: {}", userRegistrationDto.getPhone());

            throw new RegistrationException("Phone is already in use");

        }


        var user = new User(userRegistrationDto.getLogin(), userRegistrationDto.getPassword());

        userRepository.save(user);


        var phoneNumber = new PhoneNumber(userRegistrationDto.getPhone(), user);

        phoneNumberRepository.save(phoneNumber);


        var emailAddress = new EmailAddress(userRegistrationDto.getEmail(), user);

        emailAddressRepository.save(emailAddress);


        user.getEmailAddresses().add(emailAddress);

        user.getPhoneNumbers().add(phoneNumber);

        userRepository.save(user);


        var wallet = new Wallet(userRegistrationDto.getStartDeposit(), user);

        walletRepository.save(wallet);


        logger.info("User registration successful for login: {}", userRegistrationDto.getLogin());

        return getUserAuthDto(user);

    }

    private UserAuthDto getUserAuthDto(User user) {
        var token = jwtService.generateToken(user);
        return new UserAuthDto(token);
    }


}
