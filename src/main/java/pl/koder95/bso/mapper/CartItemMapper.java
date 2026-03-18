package pl.koder95.bso.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.CartItemRequestDto;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.model.CartItem;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem toModel(CartItemRequestDto cartItemDto, @Context Book book);

    @AfterMapping
    default void setBook(@MappingTarget CartItem cartItem, @Context Book book) {
        cartItem.setBook(book);
    }
}
