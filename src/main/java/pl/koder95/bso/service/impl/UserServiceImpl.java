package pl.koder95.bso.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.koder95.bso.dto.UserRegistrationRequestDto;
import pl.koder95.bso.dto.UserResponseDto;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.exception.RegistrationException;
import pl.koder95.bso.mapper.UserMapper;
import pl.koder95.bso.model.RoleName;
import pl.koder95.bso.model.User;
import pl.koder95.bso.repository.RoleRepository;
import pl.koder95.bso.repository.UserRepository;
import pl.koder95.bso.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "Email %s already exists".formatted(requestDto.getEmail())
            );
        }
        User model = userMapper.toModel(requestDto);
        model.getRoles().add(roleRepository.findByName(RoleName.ROLE_USER));
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        User saved = userRepository.save(model);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDto get(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"))
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
        }
        optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }
}
