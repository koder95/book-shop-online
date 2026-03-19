package pl.koder95.bso.mapper;

import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.CartItemResponseDto;
import pl.koder95.bso.dto.ShoppingCartResponseDto;
import pl.koder95.bso.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    @Mapping(target = "userId", source = "user.id")
    ShoppingCartResponseDto toResponseDto(ShoppingCart model,
                                          @Context List<CartItemResponseDto> cartItems);
}
