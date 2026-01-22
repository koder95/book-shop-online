package pl.koder95.bso.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.koder95.bso.dto.RegisterUserRequestDto;
import pl.koder95.bso.dto.UserDto;
import pl.koder95.bso.exception.RegistrationException;
import pl.koder95.bso.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/registration")
    UserDto register(@Valid RegisterUserRequestDto registerUserRequestDto)
            throws RegistrationException {
        return userService.register(registerUserRequestDto);
    }
}
