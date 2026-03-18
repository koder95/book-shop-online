package pl.koder95.bso.mapper;

import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.CartItemResponseDto;
import pl.koder95.bso.dto.ShoppingCartResponseDto;
import pl.koder95.bso.model.ShoppingCart;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toResponseDto(ShoppingCart model,
                                          @Context List<CartItemResponseDto> cartItems);
}
