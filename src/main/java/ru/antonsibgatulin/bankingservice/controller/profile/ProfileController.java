package ru.antonsibgatulin.bankingservice.controller.profile;


import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import io.swagger.v3.oas.annotations.media.Content;

import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.antonsibgatulin.bankingservice.dto.user.request.UserSetUpProfileDto;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import ru.antonsibgatulin.bankingservice.dto.user.request.*;

import ru.antonsibgatulin.bankingservice.dto.user.response.UserDto;
import ru.antonsibgatulin.bankingservice.except.AlreadyExistException;

import ru.antonsibgatulin.bankingservice.except.BadRequestException;

import ru.antonsibgatulin.bankingservice.except.NotFoundException;


@Validated
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "User change profile", description = "Change profile information about user")
public class ProfileController {


    private final ProfileService profileService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update user profile")
    @ApiResponse(responseCode = "200", description = "User profile updated successfully")
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    @ApiResponse(responseCode = "409", description = "Phone number or email already exists", content = @Content)
    @PostMapping("/setup")
    public ResponseEntity<Void> updateProfile(Authentication authentication, @Valid @RequestBody UserSetUpProfileDto userDto) {
        profileService.updateProfile(authentication, userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add phone number to user profile")
    @ApiResponse(responseCode = "200", description = "Phone number added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    @ApiResponse(responseCode = "409", description = "Phone number already exists", content = @Content)
    @PostMapping("/phone")
    public ResponseEntity<Void> addPhoneNumber(Authentication authentication, @Valid @RequestBody ProfileAddPhoneDto profileAddPhoneDto) {
        profileService.addPhoneNumber(authentication, profileAddPhoneDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add email address to user profile")
    @ApiResponse(responseCode = "200", description = "Email address added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email address already exists", content = @Content)
    @PostMapping("/email")
    public ResponseEntity<Void> addEmailAddress(Authentication authentication, @Valid @RequestBody ProfileAddEmailDto profileAddEmailDto) {
        profileService.addEmailAddress(authentication, profileAddEmailDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/phone/{phoneId}")
    @Operation(summary = "Delete a phone number by ID")
    @ApiResponse(responseCode = "200", description = "Phone number deleted successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "404", description = "Phone number not found", content = @Content)
    public ResponseEntity<Void> deletePhone(@Parameter(description = "Phone number ID", required = true) @PathVariable Long phoneId, Authentication authentication) {
        profileService.deletePhone(authentication, phoneId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/email/{emailId}")
    @Operation(summary = "Delete an email address by ID")
    @ApiResponse(responseCode = "200", description = "Email address deleted successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "404", description = "Email address not found", content = @Content)
    public ResponseEntity<Void> deleteEmail(@Parameter(description = "Email address ID", required = true) @PathVariable Long emailId, Authentication authentication) {
        profileService.deleteEmail(authentication, emailId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SecurityRequirement(name = "JWT")
    @PutMapping("/phone/{phoneId}")
    @Operation(summary = "Change a phone number by ID")
    @ApiResponse(responseCode = "200", description = "Phone number changed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "404", description = "Phone number not found", content = @Content)
    @ApiResponse(responseCode = "409", description = "Phone number already exists", content = @Content)
    public ResponseEntity<Void> changePhoneNumber(@Parameter(description = "Phone number ID", required = true) @PathVariable Long phoneId, @Valid @RequestBody ProfileChangePhoneDto profileChangePhoneDto, Authentication authentication) {
        profileService.changePhoneNumber(authentication, phoneId, profileChangePhoneDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SecurityRequirement(name = "JWT")
    @PutMapping("/email/{emailId}")
    @Operation(summary = "Change an email address by ID")
    @ApiResponse(responseCode = "200", description = "Email address changed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "404", description = "Email address not found", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email address already exists", content = @Content)
    public ResponseEntity<Void> changeEmailAddress(@Parameter(description = "Email address ID", required = true) @PathVariable Long emailId, @Valid @RequestBody ProfileChangeEmailDto profileChangeEmailDto, Authentication authentication) {
        profileService.changeEmailAddress(authentication, emailId, profileChangeEmailDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get user profile", description = "Retrieves the profile of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(profileService.getMe(authentication));
    }


}