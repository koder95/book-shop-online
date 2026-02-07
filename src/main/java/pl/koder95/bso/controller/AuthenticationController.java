package pl.koder95.bso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.koder95.bso.dto.RegisterUserRequestDto;
import pl.koder95.bso.dto.UserResponseDto;
import pl.koder95.bso.exception.RegistrationException;
import pl.koder95.bso.service.UserService;

@Tag(name = "Authentication management",
        description = "Provide authentication abilities and user management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    @Operation(summary = "Register a new user")
    @PostMapping("/registration")
    public UserResponseDto register(@Valid @RequestBody RegisterUserRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }
}
