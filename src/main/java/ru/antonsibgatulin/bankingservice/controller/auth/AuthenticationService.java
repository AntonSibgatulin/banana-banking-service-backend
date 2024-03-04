package ru.antonsibgatulin.bankingservice.controller.auth;

import lombok.RequiredArgsConstructor;
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

@Validated
@RequiredArgsConstructor
@Component
public class AuthenticationService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    private final EmailAddressRepository emailAddressRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    private final JwtService jwtService;


    public UserAuthDto signIn(UserAuthenticationDto userAuthenticationDto) {
        var user = userRepository.getUserByUsernameAndPassword(userAuthenticationDto.getLogin(), userAuthenticationDto.getPassword());
        if (user == null) {
            throw new AuthenticationException("Authorization failed");
        }
        return getUserAuthDto(user);
    }

    public UserAuthDto singUp(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByUsername(userRegistrationDto.getLogin())) {
            throw new RegistrationException("Login is already in use");
        }
        if (emailAddressRepository.existsByAddress(userRegistrationDto.getEmail())) {
            throw new RegistrationException("Email is already in use");
        }
        if (phoneNumberRepository.existsByNumber(userRegistrationDto.getPhone())) {
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

        return getUserAuthDto(user);

    }

    private UserAuthDto getUserAuthDto(User user) {
        var token = jwtService.generateToken(user);
        return new UserAuthDto(token);
    }


}
