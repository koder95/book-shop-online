package pl.koder95.bso.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.BookDtoWithoutCategoryIds;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.dto.CreateBookRequestDto;
import pl.koder95.bso.dto.UpdateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto book);
    
    Page<BookDto> findAll(Pageable pageable);

    BookDto get(Long id);

    BookDto update(Long id, UpdateBookRequestDto book);

    void delete(Long id);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable);
}
