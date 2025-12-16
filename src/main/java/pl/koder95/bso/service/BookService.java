package pl.koder95.bso.service;

import java.util.List;
import pl.koder95.bso.model.Book;

public interface BookService {
    Book save(Book book);
    
    List<Book> findAll();
}
