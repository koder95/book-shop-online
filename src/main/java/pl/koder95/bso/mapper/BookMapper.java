package pl.koder95.bso.mapper;

import org.mapstruct.AfterMapping;
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

    Book toModel(CreateBookRequestDto dto);

    Book toModel(BookDto dto);

    void updateModel(@MappingTarget Book model, CreateBookRequestDto dto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .distinct()
                .toList()
        );
    }
}
