package pl.koder95.bso.repository.book.spec;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.SpecificationProvider;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class MaxPriceBookSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getParameterName() {
        return "price-max";
    }

    @Override
    public Specification<Book> getSpecification(BookSearchParametersDto params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("price"), params.priceMax());
    }
}
