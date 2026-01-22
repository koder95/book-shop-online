package pl.koder95.bso.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.RegisterUserRequestDto;
import pl.koder95.bso.dto.UserDto;
import pl.koder95.bso.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(RegisterUserRequestDto registerUserRequestDto);

    User toModel(UserDto userDto);

    UserDto toDto(User user);

    void updateModel(@MappingTarget User model, RegisterUserRequestDto dto);
}
