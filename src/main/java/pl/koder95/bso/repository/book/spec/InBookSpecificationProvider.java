package pl.koder95.bso.repository.book.spec;

import lombok.RequiredArgsConstructor;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.SpecificationProvider;

@RequiredArgsConstructor
public abstract class InBookSpecificationProvider implements SpecificationProvider<Book> {

}
