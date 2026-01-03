package pl.koder95.bso.repository;

import org.springframework.data.jpa.domain.Specification;
import pl.koder95.bso.dto.BookSearchParametersDto;

public interface SpecificationBuilder<T> {
    SpecificationManager<T> getSpecificationManager();

    default Specification<T> build(BookSearchParametersDto params) {
        return getSpecificationManager().compile(params);
    }
}
