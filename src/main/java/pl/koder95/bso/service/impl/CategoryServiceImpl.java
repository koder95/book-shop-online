package pl.koder95.bso.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.koder95.bso.dto.CategoryDto;
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
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cannot find a category by id: " + id)
                );
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        return categoryMapper.toDto(
                categoryRepository.save(categoryMapper.toModel(categoryDto))
        );
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find a category by id: " + id)
        );
        categoryMapper.updateModel(category, categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
