package pl.koder95.bso.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import pl.koder95.bso.repository.book.spec.TitleBookSpecificationProvider;
import pl.koder95.bso.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private SpecificationBuilder<Book> specificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void save_correctFullRequestDto_ok() {
        // given
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("title");
        request.setAuthor("author");
        request.setCategoryIds(List.of(1L, 2L));
        request.setIsbn("1234567890");
        request.setPrice(BigDecimal.TEN);
        request.setDescription("description");
        request.setCoverImage("coverImage");
        BookDto expectedDto = new BookDto();
        expectedDto.setTitle("title");
        expectedDto.setAuthor("author");
        expectedDto.setCategoryIds(List.of(1L, 2L));
        expectedDto.setIsbn("1234567890");
        expectedDto.setPrice(BigDecimal.TEN);
        expectedDto.setDescription("description");
        expectedDto.setCoverImage("coverImage");
        expectedDto.setId(1L);
        Category firstExpected = new Category();
        firstExpected.setId(1L);
        Category secondExpected = new Category();
        secondExpected.setId(2L);
        Book toSave = new Book();
        toSave.setTitle("title");
        toSave.setAuthor("author");
        toSave.setCategories(Set.of(firstExpected, secondExpected));
        toSave.setIsbn("1234567890");
        toSave.setPrice(BigDecimal.TEN);
        toSave.setDescription("description");
        toSave.setCoverImage("coverImage");
        Book expected = new Book();
        expected.setTitle("title");
        expected.setAuthor("author");
        expected.setCategories(Set.of(firstExpected, secondExpected));
        expected.setIsbn("1234567890");
        expected.setPrice(BigDecimal.TEN);
        expected.setDescription("description");
        expected.setCoverImage("coverImage");
        expected.setId(1L);
        Mockito.when(categoryRepository.existsById(1L)).thenReturn(true);
        Mockito.when(categoryRepository.existsById(2L)).thenReturn(true);
        Mockito.when(categoryRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(firstExpected, secondExpected));
        Mockito.when(bookMapper.toDto(expected)).thenReturn(expectedDto);
        Mockito.when(bookMapper.toModel(request, List.of(firstExpected, secondExpected)))
                .thenReturn(toSave);
        Mockito.when(bookRepository.save(toSave)).thenReturn(expected);

        // when
        BookDto actualDto = bookService.save(request);

        // then
        assertEquals(expectedDto, actualDto);
        assertEquals(expectedDto.getTitle(), actualDto.getTitle());
        assertEquals(expectedDto.getAuthor(), actualDto.getAuthor());
        assertEquals(expectedDto.getCategoryIds(), actualDto.getCategoryIds());
        assertEquals(expectedDto.getIsbn(), actualDto.getIsbn());
        assertEquals(expectedDto.getPrice(), actualDto.getPrice());
        assertEquals(expectedDto.getDescription(), actualDto.getDescription());
        assertEquals(expectedDto.getCoverImage(), actualDto.getCoverImage());
        assertEquals(expectedDto.getId(), actualDto.getId());
    }

    @Test
    public void save_null_throwDpe() {
        // given, when & then
        assertThrows(DataProcessingException.class, () -> bookService.save(null));
    }

    @Test
    public void save_emptyRequestDto_throwDpE() {
        // given
        CreateBookRequestDto request = new CreateBookRequestDto();

        // when & then
        assertThrows(DataProcessingException.class, () -> bookService.save(request));
    }

    @Test
    public void save_requestWithNotExistingCategoryIds_throwDpE() {
        // given
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setCategoryIds(List.of(1L, 2L));
        Mockito.when(categoryRepository.existsById(1L)).thenReturn(true);
        Mockito.when(categoryRepository.existsById(2L)).thenReturn(false);

        // when & then
        assertThrows(DataProcessingException.class, () -> bookService.save(request));
    }

    @Test
    public void save_requestWithNotExistingCategoryId_throwDpE() {
        // given
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setCategoryIds(List.of(1L));
        Mockito.when(categoryRepository.existsById(1L)).thenReturn(false);

        // when & then
        assertThrows(DataProcessingException.class, () -> bookService.save(request));
    }

    @Test
    public void save_alreadyExists_throwDpE() {
        // given
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("title");
        request.setAuthor("author");
        request.setCategoryIds(List.of(1L, 2L));
        request.setIsbn("1234567890");
        request.setPrice(BigDecimal.TEN);
        request.setDescription("description");
        request.setCoverImage("coverImage");
        Category firstExpected = new Category();
        firstExpected.setId(1L);
        Category secondExpected = new Category();
        secondExpected.setId(2L);
        Book toSave = new Book();
        toSave.setTitle("title");
        toSave.setAuthor("author");
        toSave.setCategories(Set.of(firstExpected, secondExpected));
        toSave.setIsbn("1234567890");
        toSave.setPrice(BigDecimal.TEN);
        toSave.setDescription("description");
        toSave.setCoverImage("coverImage");
        Mockito.when(categoryRepository.existsById(1L)).thenReturn(true);
        Mockito.when(categoryRepository.existsById(2L)).thenReturn(true);
        Mockito.when(categoryRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(firstExpected, secondExpected));
        Mockito.when(bookMapper.toModel(request, List.of(firstExpected, secondExpected)))
                .thenReturn(toSave);
        Mockito.when(bookRepository.save(toSave))
                .thenThrow(new IllegalArgumentException("Book already exists"));

        // when & then
        assertThrows(DataProcessingException.class, () -> bookService.save(request));
    }

    @Test
    public void findAll_emptyRepository_ok() {
        // given
        Mockito.when(bookRepository.findAll(Mockito.any(Pageable.class))).thenReturn(Page.empty());

        // when
        Page<BookDto> actual = bookService.findAll(Pageable.unpaged());

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    public void findAll_nonEmptyRepository_ok() {
        // given
        Book book = new Book();
        book.setId(1L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setCategories(Set.of());
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.TEN);
        book.setDescription("description");
        book.setCoverImage("coverImage");
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("title");
        expected.setAuthor("author");
        expected.setCategoryIds(List.of());
        expected.setIsbn("1234567890");
        expected.setPrice(BigDecimal.TEN);
        expected.setDescription("description");
        expected.setCoverImage("coverImage");
        Pageable unpaged = Pageable.unpaged();
        Mockito.when(bookRepository.findAll(unpaged)).thenReturn(new PageImpl<>(List.of(book)));
        Mockito.when(bookMapper.toDto(book)).thenReturn(expected);

        // when
        Page<BookDto> actual = bookService.findAll(unpaged);

        // then
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.getTotalElements());
        assertEquals(expected, actual.getContent().getFirst());
    }

    @Test
    public void get_negativeId_throwEnfE() {
        // given
        Long id = -1L;
        // when & then
        assertThrows(EntityNotFoundException.class, () -> bookService.get(id));
    }

    @Test
    public void get_nonExistingId_throwEnfE() {
        // given
        Long id = 1L;
        Mockito.when(bookRepository.findById(id)).thenReturn(java.util.Optional.empty());
        // when & then
        assertThrows(EntityNotFoundException.class, () -> bookService.get(id));
    }

    @Test
    public void get_existingId_ok() {
        // given
        Long id = 1L;
        Book expected = new Book();
        expected.setId(id);
        expected.setTitle("title");
        expected.setAuthor("author");
        expected.setCategories(Set.of());
        expected.setIsbn("1234567890");
        expected.setPrice(BigDecimal.TEN);
        expected.setDescription("description");
        expected.setCoverImage("coverImage");
        BookDto expectedDto = new BookDto();
        expectedDto.setId(id);
        expectedDto.setTitle("title");
        expectedDto.setAuthor("author");
        expectedDto.setCategoryIds(List.of());
        expectedDto.setIsbn("1234567890");
        expectedDto.setPrice(BigDecimal.TEN);
        expectedDto.setDescription("description");
        expectedDto.setCoverImage("coverImage");
        Mockito.when(bookRepository.findById(id)).thenReturn(java.util.Optional.of(expected));
        Mockito.when(bookMapper.toDto(expected)).thenReturn(expectedDto);
        // when
        BookDto actual = bookService.get(id);
        // then
        assertEquals(expectedDto, actual);
    }

    @Test
    public void update_nonExistingId_throwEnfE() {
        // given
        Long id = 1L;
        UpdateBookRequestDto request = new UpdateBookRequestDto();
        Mockito.when(bookRepository.findById(id)).thenReturn(java.util.Optional.empty());
        // when & then
        assertThrows(EntityNotFoundException.class, () -> bookService.update(id, request));
    }

    @Test
    public void update_existingId_ok() {
        // given
        UpdateBookRequestDto request = new UpdateBookRequestDto();
        request.setTitle("title");
        request.setAuthor("author");
        Long id1 = 1L;
        Long id2 = 2L;
        request.setCategoryIds(List.of(id1, id2));
        request.setIsbn("1234567890");
        request.setPrice(BigDecimal.TEN);
        request.setDescription("description");
        request.setCoverImage("coverImage");
        Category firstExpected = new Category();
        firstExpected.setId(id1);
        Category secondExpected = new Category();
        secondExpected.setId(id2);
        Book toUpdate = new Book();
        toUpdate.setId(id1);
        toUpdate.setTitle("oldTitle");
        toUpdate.setAuthor("oldAuthor");
        toUpdate.setCategories(Set.of());
        toUpdate.setIsbn("0987654321");
        toUpdate.setPrice(BigDecimal.ONE);
        toUpdate.setDescription("oldDescription");
        toUpdate.setCoverImage("oldCoverImage");
        BookDto expectedDto = new BookDto();
        expectedDto.setId(id1);
        expectedDto.setTitle("title");
        expectedDto.setAuthor("author");
        expectedDto.setCategoryIds(List.of(id1, id2));
        expectedDto.setIsbn("1234567890");
        expectedDto.setPrice(BigDecimal.TEN);
        expectedDto.setDescription("description");
        expectedDto.setCoverImage("coverImage");

        Mockito.when(bookRepository.findById(id1)).thenReturn(java.util.Optional.of(toUpdate));
        Mockito.when(bookRepository.save(toUpdate)).thenReturn(toUpdate);
        Mockito.when(categoryRepository.findAllById(List.of(id1, id2)))
                .thenReturn(List.of(firstExpected, secondExpected));
        Mockito.doNothing().when(bookMapper)
                .updateModel(toUpdate, request, List.of(firstExpected, secondExpected));
        Mockito.when(bookMapper.toDto(toUpdate)).thenReturn(expectedDto);
        // when
        BookDto actual = bookService.update(id1, request);
        // then
        assertEquals(List.of(id1, id2), actual.getCategoryIds());
        assertEquals(expectedDto, actual);
    }

    @Test
    public void delete_nonExistingId_throwEnfE() {
        // given
        Long id = 1L;
        Mockito.when(bookRepository.existsById(id)).thenReturn(false);
        // when & then
        assertThrows(EntityNotFoundException.class, () -> bookService.delete(id));
    }

    @Test
    public void delete_existingId_ok() {
        // given
        Long id = 1L;
        Book toDelete = new Book();
        toDelete.setId(id);
        toDelete.setTitle("title");
        toDelete.setAuthor("author");
        toDelete.setCategories(Set.of());
        toDelete.setIsbn("1234567890");
        toDelete.setPrice(BigDecimal.TEN);
        toDelete.setDescription("description");
        toDelete.setCoverImage("coverImage");
        Mockito.when(bookRepository.existsById(id)).thenReturn(true);
        // when
        bookService.delete(id);
        // then
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void search_noMatches_ok() {
        // given
        BookSearchParametersDto params = new BookSearchParametersDto(
                List.of("nonExistingTitle"), List.of(), List.of(), null, null
        );
        Mockito.when(specificationBuilder.build(params))
                .thenReturn(new TitleBookSpecificationProvider().getSpecification(params));
        Mockito.when(bookRepository
                        .findAll(specificationBuilder.build(params), Pageable.unpaged()))
                .thenReturn(Page.empty());
        // when
        Page<BookDto> actual = bookService.search(params, Pageable.unpaged());
        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    public void search_matches_ok() {
        // given
        Book book = new Book();
        book.setId(1L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setCategories(Set.of());
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.TEN);
        book.setDescription("description");
        book.setCoverImage("coverImage");
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("title");
        expected.setAuthor("author");
        expected.setCategoryIds(List.of());
        expected.setIsbn("1234567890");
        expected.setPrice(BigDecimal.TEN);
        expected.setDescription("description");
        expected.setCoverImage("coverImage");
        BookSearchParametersDto params = new BookSearchParametersDto(
                List.of("title"), List.of(), List.of(), null, null
        );
        Mockito.when(specificationBuilder.build(params))
                .thenReturn(new TitleBookSpecificationProvider().getSpecification(params));
        Pageable pageable = Pageable.unpaged();
        Mockito.when(bookRepository.findAll(specificationBuilder.build(params), pageable))
                .thenReturn(new PageImpl<>(List.of(book)));
        Mockito.when(bookMapper.toDto(book)).thenReturn(expected);
        // when
        Page<BookDto> actual = bookService.search(params, pageable);
        // then
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.getTotalElements());
        assertEquals(expected, actual.getContent().getFirst());
    }

    @Test
    public void findAllByCategoryId_noMatches_ok() {
        // given
        Long categoryId = 1L;
        Pageable pageable = Pageable.unpaged();
        Mockito.when(bookRepository.findAllByCategories_Id(categoryId, pageable))
                .thenReturn(Page.empty());
        // when
        Page<BookDtoWithoutCategoryIds> actual = bookService
                .findAllByCategoryId(categoryId, pageable);
        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    public void findAllByCategoryId_matches_ok() {
        // given
        Long categoryId = 1L;
        Book book = new Book();
        book.setId(categoryId);
        book.setTitle("title");
        book.setAuthor("author");
        book.setCategories(Set.of());
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.TEN);
        book.setDescription("description");
        book.setCoverImage("coverImage");
        BookDtoWithoutCategoryIds expected = new BookDtoWithoutCategoryIds(
                categoryId, "title", "author", "1234567890",
                BigDecimal.TEN, "description", "coverImage"
        );
        Pageable pageable = Pageable.unpaged();
        Mockito.when(bookRepository.findAllByCategories_Id(categoryId, pageable))
                .thenReturn(new PageImpl<>(List.of(book)));
        Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(expected);
        // when
        Page<BookDtoWithoutCategoryIds> actual = bookService
                .findAllByCategoryId(categoryId, pageable);
        // then
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.getTotalElements());
        assertEquals(expected, actual.getContent().getFirst());
    }
}
