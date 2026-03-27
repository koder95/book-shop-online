package pl.koder95.bso.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.OrderItemResponseDto;
import pl.koder95.bso.model.CartItem;
import pl.koder95.bso.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toResponseDto(OrderItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", source = "book")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "book.price")
    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItem(CartItem cartItem);
}
