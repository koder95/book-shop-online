package pl.koder95.bso.dto;

import pl.koder95.bso.dto.validation.FieldMatch;

@FieldMatch(first = "password", second = "repeatPassword")
public record UpdateUserRequestDto(
        String firstName,
        String lastName,
        String password,
        String repeatPassword,
        String shippingAddress
) {
}
