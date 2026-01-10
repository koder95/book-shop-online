package pl.koder95.bso.repository.book.spec;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.koder95.bso.dto.BookSearchParametersDto;

@RequiredArgsConstructor
@Component
public class IsbnBookSpecificationProvider extends InBookSpecificationProvider {

    @Override
    public String getParameterName() {
        return "isbn";
    }

    @Override
    public List<String> getValues(BookSearchParametersDto params) {
        return params == null ? List.of() : params.isbns();
    }
}
