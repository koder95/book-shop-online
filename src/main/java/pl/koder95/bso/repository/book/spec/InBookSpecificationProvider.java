package pl.koder95.bso.repository.book.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.jpa.domain.Specification;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.SpecificationProvider;

public abstract class InBookSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public Specification<Book> getSpecification(BookSearchParametersDto params) {
        return (root, query, criteriaBuilder) -> {
            String paramName = getParameterName();
            Path<String> paramNamePath = root.get(paramName);
            List<String> values = getValues(params);
            if (values == null || values.isEmpty()) {
                return likeIgnoreCase(criteriaBuilder, paramNamePath, "");
            }
            return values.stream()
                    .map(value -> likeIgnoreCase(criteriaBuilder, paramNamePath, value))
                    .reduce(criteriaBuilder::or)
                    .orElseThrow(() -> new NoSuchElementException(
                            "No specification provider for parameter: " + paramName
                    ));
        };
    }

    private static Predicate likeIgnoreCase(CriteriaBuilder criteriaBuilder,
                                            Path<String> paramNamePath,
                                            String value) {
        Expression<String> lowerParamName = criteriaBuilder.lower(paramNamePath);
        return criteriaBuilder.like(lowerParamName, '%' + value.toLowerCase() + '%');
    }

    public abstract List<String> getValues(BookSearchParametersDto params);
}
