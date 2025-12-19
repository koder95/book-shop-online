package pl.koder95.bso.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.CreateBookRequestDto;
import pl.koder95.bso.exception.DataProcessingException;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.mapper.BookMapper;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.repository.BookRepository;
import pl.koder95.bso.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto book) {
        try {
            Book saved = bookRepository.save(bookMapper.toModel(book));
            return bookMapper.toDto(saved);
        } catch (Exception e) {
            throw new DataProcessingException("Cannot save book: " + book, e);
        }
    }

    @Override
    public List<BookDto> findAll() {
        try {
            return bookRepository.findAll().stream()
                    .map(bookMapper::toDto)
                    .toList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot find all books", e);
        }
    }

    @Override
    public BookDto get(Long id) {
        try {
            return bookRepository.findById(id)
                    .map(bookMapper::toDto)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Entity with id " + id + " not found")
                    );
        } catch (Exception e) {
            throw new EntityNotFoundException("Cannot find book by id: " + id);
        }
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto book) {
        Book model = bookMapper.toModel(get(id));
        bookMapper.updateModel(model, book);
        try {
            Book updated = bookRepository.save(model);
            return bookMapper.toDto(updated);
        } catch (Exception e) {
            throw new DataProcessingException("Cannot update book: " + book, e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataProcessingException("Cannot delete book: " + id, e);
        }
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
}
