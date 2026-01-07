package pl.koder95.bso.repository.book.spec;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.koder95.bso.dto.BookSearchParametersDto;

@RequiredArgsConstructor
@Component
public class AuthorBookSpecificationProvider extends InBookSpecificationProvider {

    @Override
    public String getParameterName() {
        return "author";
    }

    @Override
    public Object[] getValues(BookSearchParametersDto params) {
        return params == null ? new Object[0] : params.authors();
    }
}
