package pl.koder95.bso.controller;

import java.util.List;
import pl.koder95.bso.model.Book;

public interface BookService {
    Book save(Book book);
    
    List<Book> findAll();
}
