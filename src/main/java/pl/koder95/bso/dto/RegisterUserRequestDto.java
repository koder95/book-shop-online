package pl.koder95.bso.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pl.koder95.bso.dto.validation.CommonValue;
import pl.koder95.bso.dto.validation.ValidateCommonValues;

@Data
@ValidateCommonValues
public class RegisterUserRequestDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @CommonValue
    private String password;
    @NotBlank
    @CommonValue
    private String repeatPassword;
    private String shippingAddress;
}
