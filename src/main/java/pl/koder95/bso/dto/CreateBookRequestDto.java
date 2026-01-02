package pl.koder95.bso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;
import org.jspecify.annotations.NonNull;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;
    @NonNull
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
}
