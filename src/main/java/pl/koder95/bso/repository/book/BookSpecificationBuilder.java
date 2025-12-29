package pl.koder95.bso.repository.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.SpecificationBuilder;
import pl.koder95.bso.repository.SpecificationManager;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationManager<Book> specificationManager;

    @Override
    public SpecificationManager<Book> getSpecificationManager() {
        return specificationManager;
    }
}
