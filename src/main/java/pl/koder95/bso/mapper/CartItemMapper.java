package pl.koder95.bso.mapper;

import org.mapstruct.Mapper;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.CartItemRequestDto;
import pl.koder95.bso.dto.CartItemResponseDto;
import pl.koder95.bso.model.CartItem;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem toModel(CartItemRequestDto cartItemDto);

    CartItem toModel(CartItemResponseDto cartItemDto);
}
