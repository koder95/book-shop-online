package pl.koder95.bso.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto book);
    
    List<BookDto> findAll(Pageable pageable);

    BookDto get(Long id);

    BookDto update(Long id, CreateBookRequestDto book);

    void delete(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
