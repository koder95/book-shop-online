package pl.koder95.bso.service;

import pl.koder95.bso.dto.CartItemRequestDto;
import pl.koder95.bso.dto.ShoppingCartResponseDto;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCart();

    ShoppingCartResponseDto addItem(CartItemRequestDto cartItemDto);

    ShoppingCartResponseDto updateItem(Long id, int quantity);

    void deleteItem(Long id);
}
