package pl.koder95.bso.repository.book.spec;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthorBookSpecificationProvider extends InBookSpecificationProvider {

    @Override
    public String getParameterName() {
        return "author";
    }
}
