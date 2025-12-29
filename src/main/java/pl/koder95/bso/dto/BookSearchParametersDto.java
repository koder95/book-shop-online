package pl.koder95.bso.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestParam;

public record BookSearchParametersDto(String[] titles,
                                      String[] authors,
                                      String[] isbns,
                                      BigDecimal priceMin,
                                      BigDecimal priceMax) {

    public BookSearchParametersDto(@RequestParam(required = false) String[] titles,
                                   @RequestParam(required = false) String[] authors,
                                   @RequestParam(required = false) String[] isbns,
                                   @RequestParam(required = false, name = "price-min") BigDecimal priceMin,
                                   @RequestParam(required = false, name = "price-max") BigDecimal priceMax) {
        this.titles = titles == null ? new String[0] : Arrays.stream(titles).toArray(String[]::new);
        this.authors = authors == null ? new String[0] : Arrays.stream(authors).toArray(String[]::new);
        this.isbns = isbns == null ? new String[0] : Arrays.stream(isbns).toArray(String[]::new);
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        System.out.println("Created: " + this);
    }

    @Override
    public String toString() {
        return "BookSearchParametersDto{" +
                "titles=" + Arrays.toString(titles) +
                ", authors=" + Arrays.toString(authors) +
                ", isbns=" + Arrays.toString(isbns) +
                ", priceMin=" + priceMin +
                ", priceMax=" + priceMax +
                '}';
    }
}
