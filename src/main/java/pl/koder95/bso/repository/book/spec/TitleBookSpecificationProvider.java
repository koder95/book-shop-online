package pl.koder95.bso.repository.book.spec;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.koder95.bso.dto.BookSearchParametersDto;

@RequiredArgsConstructor
@Component
public class TitleBookSpecificationProvider extends InBookSpecificationProvider {

    @Override
    public String getParameterName() {
        return "title";
    }

    @Override
    public Object[] getValues(BookSearchParametersDto params) {
        return params == null ? new Object[0] : params.titles();
    }
}
