package pl.koder95.bso.mapper;

import java.util.HashSet;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.BookDtoWithoutCategoryIds;
import pl.koder95.bso.dto.CreateBookRequestDto;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.model.Category;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto book, @Context List<Category> allById);

    Book toModel(BookDto dto);

    void updateModel(@MappingTarget Book model, CreateBookRequestDto dto,
                     @Context List<Category> allById);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .distinct()
                .toList()
        );
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, @Context List<Category> allById) {
        book.setCategories(new HashSet<>(allById));
    }
}
