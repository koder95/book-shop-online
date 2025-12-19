package pl.koder95.bso.mapper;

import org.mapstruct.Mapper;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.BookDto;
import pl.koder95.bso.dto.CreateBookRequestDto;
import pl.koder95.bso.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto dto);
}
