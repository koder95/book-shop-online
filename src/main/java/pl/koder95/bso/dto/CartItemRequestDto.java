package pl.koder95.bso.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @Min(1)
        @NotNull
        Long bookId,
        @Min(1)
        @NotNull
        Integer quantity
) {
}
