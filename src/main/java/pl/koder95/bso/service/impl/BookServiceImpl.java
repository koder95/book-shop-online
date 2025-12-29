package pl.koder95.bso.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.dto.CreateBookRequestDto;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.mapper.BookMapper;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.BookRepository;
import pl.koder95.bso.repository.SpecificationBuilder;
import pl.koder95.bso.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SpecificationBuilder<Book> specificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto book) {
        Book saved = bookRepository.save(bookMapper.toModel(book));
        return bookMapper.toDto(saved);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto get(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Entity with id " + id + " not found")
                );
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto book) {
        Book model = bookMapper.toModel(book);
        model.setId(id);
        Book updated = bookRepository.save(model);
        return bookMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params) {
        return bookRepository.findAll(specificationBuilder.build(params)).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
