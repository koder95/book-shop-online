package pl.koder95.bso.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.SpecificationManager;
import pl.koder95.bso.repository.SpecificationProvider;

@RequiredArgsConstructor
@Component
public class BookSpecificationManager implements SpecificationManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String parameterName) {
        return specificationProviders.stream()
                .filter(spec -> spec.getParameterName().equals(parameterName))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public Specification<Book> compile(BookSearchParametersDto params) {
        Specification<Book> spec = Specification.unrestricted();
        if (params.authors() != null && params.authors().length > 0) {
            spec = spec.and(getSpecificationProvider("author")
                    .getSpecification(params));
        }
        if (params.titles() != null && params.titles().length > 0) {
            spec = spec.and(getSpecificationProvider("title")
                    .getSpecification(params));
        }
        if (params.isbns() != null && params.isbns().length > 0) {
            spec = spec.and(getSpecificationProvider("isbn")
                    .getSpecification(params));
        }
        if (params.priceMin() != null) {
            spec = spec.and(getSpecificationProvider("price-min")
                    .getSpecification(params));
        }
        if (params.priceMax() != null) {
            spec = spec.and(getSpecificationProvider("price-max")
                    .getSpecification(params));
        }
        return spec;
    }
}
