package pl.koder95.bso.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pl.koder95.bso.dto.validation.FieldMatch;

@Data
@FieldMatch(first = "password", second = "repeatPassword")
public class RegisterUserRequestDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    private String shippingAddress;
}
