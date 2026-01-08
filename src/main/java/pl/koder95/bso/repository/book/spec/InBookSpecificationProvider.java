package pl.koder95.bso.repository.book.spec;

import jakarta.persistence.criteria.Path;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.SpecificationProvider;

@RequiredArgsConstructor
public abstract class InBookSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public Specification<Book> getSpecification(BookSearchParametersDto params) {
        return (root, query, criteriaBuilder) -> {
            Path<String> paramName = root.get(getParameterName());
            return getValues(params).stream()
                    .map(value -> '%' + value.toLowerCase() + '%')
                    .map(value -> criteriaBuilder.like(criteriaBuilder.lower(paramName), value))
                    .reduce(criteriaBuilder::or)
                    .orElseThrow(() -> new NoSuchElementException(
                            "No specification provider for: " + paramName
                    ));
        };
    }

    public abstract List<String> getValues(BookSearchParametersDto params);
}
