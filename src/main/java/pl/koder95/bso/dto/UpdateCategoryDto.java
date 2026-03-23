package pl.koder95.bso.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCategoryDto(
        @NotNull
        @Min(1)
        Long id,
        @NotBlank
        @Size(max = 255)
        String name,
        @Size(max = 1000)
        String description
) {
}
