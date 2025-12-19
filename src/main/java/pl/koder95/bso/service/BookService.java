package pl.koder95.bso.service;

import java.util.List;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto book);
    
    List<BookDto> findAll();

    BookDto get(Long id);
}
