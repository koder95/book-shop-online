package pl.koder95.bso.dto;

import java.math.BigDecimal;
import java.util.List;

public record BookSearchParametersDto(List<String> titles,
                                      List<String> authors,
                                      List<String> isbns,
                                      BigDecimal priceMin,
                                      BigDecimal priceMax) {

    public BookSearchParametersDto {
        titles = titles == null ? List.of() : List.copyOf(titles);
        authors = authors == null ? List.of() : List.copyOf(authors);
        isbns = isbns == null ? List.of() : List.copyOf(isbns);
    }

    @Override
    public String toString() {
        return "BookSearchParametersDto{"
                + "titles=" + titles
                + ", authors=" + authors
                + ", isbns=" + isbns
                + ", priceMin=" + priceMin
                + ", priceMax=" + priceMax
                + '}';
    }
}
