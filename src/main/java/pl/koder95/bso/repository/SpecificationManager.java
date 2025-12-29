package pl.koder95.bso.repository;

import org.springframework.data.jpa.domain.Specification;
import pl.koder95.bso.dto.BookSearchParametersDto;

public interface SpecificationManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String parameterName);

    Specification<T> compile(BookSearchParametersDto params);
}
