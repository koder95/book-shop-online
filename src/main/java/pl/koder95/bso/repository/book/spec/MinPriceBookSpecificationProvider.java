package pl.koder95.bso.repository.book.spec;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.SpecificationProvider;

@RequiredArgsConstructor
@Component
public class MinPriceBookSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getParameterName() {
        return "price-min";
    }

    @Override
    public Specification<Book> getSpecification(BookSearchParametersDto params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("price"), params.priceMin());
    }
}
