package pl.koder95.bso.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginResponseDto(@NotBlank String token) {
}
