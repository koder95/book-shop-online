package pl.koder95.bso.repository.book;

import java.util.List;
import java.util.NoSuchElementException;
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
                .orElseThrow(() -> new NoSuchElementException(
                        "No specification provider for: " + parameterName
                ));
    }

    @Override
    public Specification<Book> compile(BookSearchParametersDto params) {
        return specificationProviders.stream()
                .map(p -> p.getSpecification(params))
                .reduce(Specification.unrestricted(), Specification::and);
    }
}
