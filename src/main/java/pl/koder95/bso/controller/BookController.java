package pl.koder95.bso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.BookSearchParametersDto;
import pl.koder95.bso.dto.CreateBookRequestDto;
import pl.koder95.bso.service.BookService;

@Tag(name = "Book management", description = "Endpoints for managing products")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Get all books",
            description = "Get a list of all available books")
    @GetMapping
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Get a book by id",
            description = "Get a book entity by identity number (id)")
    @GetMapping("/{id}")
    public BookDto get(@PathVariable Long id) {
        return bookService.get(id);
    }

    @Operation(summary = "Create a new book",
            description = "Create a new book")
    @PostMapping
    public BookDto create(@Valid @RequestBody CreateBookRequestDto createRequest) {
        return bookService.save(createRequest);
    }

    @Operation(summary = "Update the book specified by id",
            description = "Change a properties of the book specified by identity number (id).")
    @PutMapping("/{id}")
    public BookDto update(@PathVariable Long id, @RequestBody CreateBookRequestDto book) {
        return bookService.update(id, book);
    }

    @Operation(summary = "Remove the book specified by id",
            description = "Remove the book specified by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @Operation(summary = "Search books matching the search parameters",
            description = "Get a list of books matching the search parameters such as title, "
                    + "author, isbn, minimum and maximum price.")
    @GetMapping("/search")
    public List<BookDto> search(@ModelAttribute BookSearchParametersDto params) {
        return bookService.search(params);
    }
}
