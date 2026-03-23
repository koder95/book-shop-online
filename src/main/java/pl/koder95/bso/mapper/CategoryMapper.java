package pl.koder95.bso.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.CategoryResponseDto;
import pl.koder95.bso.dto.CreateCategoryRequestDto;
import pl.koder95.bso.dto.UpdateCategoryDto;
import pl.koder95.bso.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toResponseDto(Category category);

    Category toModel(CreateCategoryRequestDto categoryDto);

    void updateModel(@MappingTarget Category model, UpdateCategoryDto dto);
}
