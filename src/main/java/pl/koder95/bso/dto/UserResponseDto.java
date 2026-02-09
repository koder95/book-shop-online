package pl.koder95.bso.dto;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String shippingAddress
) {
}
