package ru.antonsibgatulin.bankingservice.controller.search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.antonsibgatulin.bankingservice.dto.user.response.UserDto;

import java.util.List;
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Search API", description = "API for searching users")

public class SearchController {


    private final SearchService searchService;


    @SecurityRequirement(name = "JWT")
    @GetMapping("")
    @Operation(summary = "Search users", description = "Searches users based on the provided search criteria")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "List of users that match the search criteria"),

            @ApiResponse(responseCode = "400", description = "Invalid search criteria")

    })
    public ResponseEntity<List<UserDto>> searchUsers(
            @RequestParam(value = "dateOfBirth", required = false) String dateOfBirth,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder
    ) {

        List<UserDto> users = searchService.searchUsers(dateOfBirth, phone, fullName, email, page, size, sortBy, sortOrder);
        return ResponseEntity.ok(users);

    }

}