package pl.koder95.bso.repository.book.spec;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.SpecificationProvider;

@RequiredArgsConstructor
public abstract class InBookSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public Specification<Book> getSpecification(BookSearchParametersDto params) {
        return (root, query, criteriaBuilder) -> root
                .get(getParameterName())
                .in((Object[]) params.authors());
    }
}
