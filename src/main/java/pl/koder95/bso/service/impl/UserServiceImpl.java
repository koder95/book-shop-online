package pl.koder95.bso.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(RegisterUserRequestDto requestDto) throws RegistrationException {
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

    @Component
    public static class PasswordEncoder {
        public String encode(String password) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("sha-512");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            return Base64.getEncoder().encodeToString(md.digest(password.getBytes()));
        }
    }
}
