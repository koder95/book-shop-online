package pl.koder95.bso.service;

import java.util.Optional;
import pl.koder95.bso.dto.RegisterUserRequestDto;
import pl.koder95.bso.dto.UpdateUserRequestDto;
import pl.koder95.bso.dto.UserDto;
import pl.koder95.bso.exception.RegistrationException;

public interface UserService {
    UserDto register(RegisterUserRequestDto requestDto) throws RegistrationException;

    UserDto get(Long id);

    UserDto update(Long id, UpdateUserRequestDto dto);

    void delete(Long id);

    Optional<UserDto> findByEmail(String email);
}
