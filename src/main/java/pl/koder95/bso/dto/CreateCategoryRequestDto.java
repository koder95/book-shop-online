package pl.koder95.bso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotBlank
        @Size(max = 255)
        String name,
        @Size(max = 1000)
        String description
) {
}
