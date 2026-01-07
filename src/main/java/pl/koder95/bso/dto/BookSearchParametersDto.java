package pl.koder95.bso.dto;

import java.math.BigDecimal;
import java.util.Arrays;

public record BookSearchParametersDto(String[] titles,
                                      String[] authors,
                                      String[] isbns,
                                      BigDecimal priceMin,
                                      BigDecimal priceMax) {

    public BookSearchParametersDto {
        titles = titles == null
                ? new String[0] : Arrays.stream(titles).toArray(String[]::new);
        authors = authors == null
                ? new String[0] : Arrays.stream(authors).toArray(String[]::new);
        isbns = isbns == null
                ? new String[0] : Arrays.stream(isbns).toArray(String[]::new);
    }

    @Override
    public String toString() {
        return "BookSearchParametersDto{"
                + "titles=" + Arrays.toString(titles)
                + ", authors=" + Arrays.toString(authors)
                + ", isbns=" + Arrays.toString(isbns)
                + ", priceMin=" + priceMin
                + ", priceMax=" + priceMax
                + '}';
    }
}
