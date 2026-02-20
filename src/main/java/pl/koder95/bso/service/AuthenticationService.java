package pl.koder95.bso.service;

import pl.koder95.bso.dto.UserLoginRequestDto;
import pl.koder95.bso.dto.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto request);
}
