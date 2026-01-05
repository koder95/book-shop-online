package pl.koder95.bso.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.CreateBookRequestDto;
import pl.koder95.bso.exception.DataProcessingException;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.service.BookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDto get(@PathVariable Long id) {
        try {
            return bookService.get(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), e.getMessage());
        }
    }

    @PostMapping
    public BookDto createBook(@Valid @RequestBody CreateBookRequestDto createRequest) {
        try {
            return bookService.save(createRequest);
        } catch (DataProcessingException e) {
            // TODO: relocate handle of exceptions to central management
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }
    }
}

