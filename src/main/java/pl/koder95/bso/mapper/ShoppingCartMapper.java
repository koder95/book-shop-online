package pl.koder95.bso.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.ShoppingCartResponseDto;
import pl.koder95.bso.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "cartItems")
    ShoppingCartResponseDto toResponseDto(ShoppingCart model);
}
