package pl.koder95.bso.dto;

import pl.koder95.bso.dto.validation.CommonValue;
import pl.koder95.bso.dto.validation.ValidateCommonValues;

@ValidateCommonValues
public record UpdateUserRequestDto(
        String firstName,
        String lastName,
        @CommonValue String password,
        @CommonValue String repeatPassword,
        String shippingAddress
) {
}
