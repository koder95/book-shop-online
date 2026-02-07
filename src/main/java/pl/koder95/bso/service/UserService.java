package pl.koder95.bso.service;

import java.util.Optional;
import pl.koder95.bso.dto.UserRegistrationRequestDto;
import pl.koder95.bso.dto.UserResponseDto;
import pl.koder95.bso.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserResponseDto get(Long id);

    void delete(Long id);

    Optional<UserResponseDto> findByEmail(String email);
}
