package pl.koder95.bso.repository;

import org.springframework.data.jpa.domain.Specification;
import pl.koder95.bso.dto.BookSearchParametersDto;

public interface SpecificationProvider<T> {
    String getParameterName();

    Specification<T> getSpecification(BookSearchParametersDto params);
}
