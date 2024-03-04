package ru.antonsibgatulin.bankingservice.controller.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;

import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import ru.antonsibgatulin.bankingservice.dto.user.request.UserAuthenticationDto;

import ru.antonsibgatulin.bankingservice.dto.user.request.UserRegistrationDto;

import ru.antonsibgatulin.bankingservice.dto.user.response.UserAuthDto;

import ru.antonsibgatulin.bankingservice.entity.user.User;

import ru.antonsibgatulin.bankingservice.entity.wallet.Wallet;

import ru.antonsibgatulin.bankingservice.except.AuthenticationException;

import ru.antonsibgatulin.bankingservice.except.RegistrationException;

import ru.antonsibgatulin.bankingservice.repository.UserRepository;

import ru.antonsibgatulin.bankingservice.repository.WalletRepository;

import ru.antonsibgatulin.bankingservice.service.jwt.JwtService;


import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/signin")
    @Operation(
            summary = "Sign in to the application",
            description = "Authenticates a user and returns a JWT token",
            tags = {"Authentication"},
            /*security = {
                    @SecurityRequirement(name = "bearerAuth")
            },

             */

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserAuthenticationDto.class)
                    )
            ),

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful authentication",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthDto.class)
                            )

                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )

            }

    )
    public ResponseEntity<UserAuthDto> signIn(@Valid @RequestBody UserAuthenticationDto userAuthenticationDto) {

        return ResponseEntity.ok(authenticationService.signIn(userAuthenticationDto));

    }


    @PostMapping("/signup")
    @Operation(
            summary = "Sign up for the application",
            description = "Registers a new user and creates a new wallet",
            tags = {"Authentication"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRegistrationDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful registration",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthDto.class)
                            )

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json"
                            )

                    )

            }

    )
    public ResponseEntity<UserAuthDto> signUp(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {

        return ResponseEntity.ok(authenticationService.singUp(userRegistrationDto));

    }


}