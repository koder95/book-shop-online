package pl.koder95.bso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OrderStatusUpdateDto(
        @NotBlank
        @Pattern(regexp = "PENDING|PREPARING|CANCELLED|SHIPPED|DELIVERED|RETURNED|COMPLETED")
        String status
) {
}
