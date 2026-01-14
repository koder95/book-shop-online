package pl.koder95.bso.repository.book.spec;

import java.util.List;
import org.springframework.stereotype.Component;
import pl.koder95.bso.dto.BookSearchParametersDto;

@Component
public class AuthorBookSpecificationProvider extends InBookSpecificationProvider {

    @Override
    public String getParameterName() {
        return "author";
    }

    @Override
    public List<String> getValues(BookSearchParametersDto params) {
        return params == null ? List.of() : params.authors();
    }
}
