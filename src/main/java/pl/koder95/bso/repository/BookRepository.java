package pl.koder95.bso.repository;

import java.util.List;
import java.util.Optional;
import pl.koder95.bso.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}
