package pl.koder95.bso.service.impl;

import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.BookDtoWithoutCategoryIds;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.dto.CreateBookRequestDto;
import pl.koder95.bso.dto.UpdateBookRequestDto;
import pl.koder95.bso.exception.DataProcessingException;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.mapper.BookMapper;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.model.Category;
import pl.koder95.bso.repository.BookRepository;
import pl.koder95.bso.repository.CategoryRepository;
import pl.koder95.bso.repository.SpecificationBuilder;
import pl.koder95.bso.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SpecificationBuilder<Book> specificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto book) {
        try {
            List<Long> notExists = book.getCategoryIds().stream()
                    .filter(Predicate.not(categoryRepository::existsById))
                    .toList();
            if (!notExists.isEmpty()) {
                throw new EntityNotFoundException("Category ids not found: " + notExists);
            }
            List<Category> allById = categoryRepository.findAllById(book.getCategoryIds());
            Book saved = bookRepository.save(bookMapper.toModel(book, allById));
            return bookMapper.toDto(saved);
        } catch (Exception e) {
            throw new DataProcessingException("Cannot save book: " + book, e);
        }
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        try {
            return bookRepository.findAll(pageable)
                    .map(bookMapper::toDto);
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
    public BookDto update(Long id, UpdateBookRequestDto book) {
        Book model = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Entity with id " + id + " not found")
        );
        bookMapper.updateModel(model, book, categoryRepository.findAllById(book.getCategoryIds()));
        try {
            Book updated = bookRepository.save(model);
            return bookMapper.toDto(updated);
        } catch (Exception e) {
            throw new DataProcessingException("Cannot update book: " + book, e);
        }
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id " + id + " not found");
        }
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataProcessingException("Cannot delete book: " + id, e);
        }
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        return bookRepository.findAll(specificationBuilder.build(params), pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategories_Id(categoryId, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }
}
