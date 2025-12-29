package pl.koder95.bso.repository.book.spec;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TitleBookSpecificationProvider extends InBookSpecificationProvider {

    @Override
    public String getParameterName() {
        return "title";
    }
}
