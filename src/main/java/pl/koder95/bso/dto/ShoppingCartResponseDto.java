package pl.koder95.bso.dto;

import java.util.List;

public record ShoppingCartResponseDto(Long id, Long userId, List<CartItemResponseDto> cartItems) {
}
