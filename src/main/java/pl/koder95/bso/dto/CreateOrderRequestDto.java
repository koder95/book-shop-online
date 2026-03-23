package pl.koder95.bso.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDto(@NotBlank String shippingAddress) {
}
