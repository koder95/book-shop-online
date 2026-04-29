package pl.koder95.bso.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.koder95.bso.dto.CategoryResponseDto;
import pl.koder95.bso.dto.CreateCategoryRequestDto;
import pl.koder95.bso.dto.UpdateCategoryDto;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.mapper.CategoryMapper;
import pl.koder95.bso.model.Category;
import pl.koder95.bso.repository.CategoryRepository;
import pl.koder95.bso.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponseDto);
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponseDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cannot find a category by id: " + id)
                );
    }

    @Override
    public CategoryResponseDto save(CreateCategoryRequestDto categoryDto) {
        return categoryMapper.toResponseDto(
                categoryRepository.save(categoryMapper.toModel(categoryDto))
        );
    }

    @Override
    public CategoryResponseDto update(Long id, UpdateCategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find a category by id: " + id)
        );
        categoryMapper.updateModel(category, categoryDto);
        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Cannot find a category by id: " + id);
        }
    }
}
