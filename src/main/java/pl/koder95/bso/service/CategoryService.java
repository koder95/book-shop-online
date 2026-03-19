package pl.koder95.bso.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.koder95.bso.dto.CategoryResponseDto;
import pl.koder95.bso.dto.CreateCategoryRequestDto;
import pl.koder95.bso.dto.UpdateCategoryDto;

public interface CategoryService {
    Page<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CreateCategoryRequestDto categoryDto);

    CategoryResponseDto update(Long id, UpdateCategoryDto categoryDto);

    void deleteById(Long id);
}
