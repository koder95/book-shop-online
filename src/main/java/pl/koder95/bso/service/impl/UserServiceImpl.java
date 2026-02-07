package pl.koder95.bso.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.koder95.bso.dto.RegisterUserRequestDto;
import pl.koder95.bso.dto.UpdateUserRequestDto;
import pl.koder95.bso.dto.UserResponseDto;
import pl.koder95.bso.exception.RegistrationException;
import pl.koder95.bso.mapper.UserMapper;
import pl.koder95.bso.model.User;
import pl.koder95.bso.repository.UserRepository;
import pl.koder95.bso.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(RegisterUserRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "Email %s already exists".formatted(requestDto.getEmail())
            );
        }
        User model = userMapper.toModel(requestDto);
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        User saved = userRepository.save(model);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDto get(Long id) {
        return userMapper.toDto(userRepository.getReferenceById(id));
    }

    @Override
    public UserResponseDto update(Long id, UpdateUserRequestDto dto) {
        User user = userRepository.getReferenceById(id);
        userMapper.updateModel(user, dto);
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserResponseDto> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }
}
