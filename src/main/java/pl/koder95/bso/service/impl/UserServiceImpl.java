package pl.koder95.bso.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.koder95.bso.dto.RegisterUserRequestDto;
import pl.koder95.bso.dto.UserDto;
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

    @Override
    public UserDto register(RegisterUserRequestDto requestDto) throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "Email %s already exists".formatted(requestDto.getEmail())
            );
        }
        return userMapper.toDto(userMapper.toModel(requestDto));
    }

    @Override
    public UserDto get(Long id) {
        return userMapper.toDto(userRepository.getReferenceById(id));
    }

    @Override
    public UserDto update(Long id, RegisterUserRequestDto dto) {
        User user = userRepository.getReferenceById(id);
        userMapper.updateModel(user, dto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }
}
