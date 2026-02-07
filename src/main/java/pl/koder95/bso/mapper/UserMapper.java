package pl.koder95.bso.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.RegisterUserRequestDto;
import pl.koder95.bso.dto.UpdateUserRequestDto;
import pl.koder95.bso.dto.UserResponseDto;
import pl.koder95.bso.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(RegisterUserRequestDto registerUserRequestDto);

    User toModel(UserResponseDto userResponseDto);

    UserResponseDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    void updateModel(@MappingTarget User model, UpdateUserRequestDto dto);
}
