package pl.koder95.bso.dto;

import jakarta.validation.constraints.Min;

public record ItemQuantityDto(@Min(1) int quantity) {
}
