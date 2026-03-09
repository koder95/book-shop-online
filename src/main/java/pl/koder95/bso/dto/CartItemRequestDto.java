package pl.koder95.bso.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        Long id,
        Long bookId,
        @NotBlank
        String bookTitle,
        @Min(1)
        @NotNull
        Integer quantity
) {
}
