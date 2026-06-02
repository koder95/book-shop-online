package pl.koder95.bso.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.koder95.bso.dto.CategoryResponseDto;
import pl.koder95.bso.dto.CreateCategoryRequestDto;
import pl.koder95.bso.dto.UpdateCategoryDto;
import pl.koder95.bso.exception.DataProcessingException;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.mapper.CategoryMapper;
import pl.koder95.bso.model.Category;
import pl.koder95.bso.repository.CategoryRepository;
import pl.koder95.bso.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void save_correctFullRequestDto_ok() {
        // given
        String testName = "Test category";
        String testDescription = "Test description";
        Category beforeSave = new Category();
        beforeSave.setName(testName);
        beforeSave.setDescription(testDescription);
        beforeSave.setDeleted(false);
        Long id = 1L;
        Category afterSave = new Category();
        afterSave.setId(id);
        afterSave.setName(testName);
        afterSave.setDescription(testDescription);
        afterSave.setDeleted(false);
        CategoryResponseDto expected = new CategoryResponseDto(
                id, testName, testDescription
        );
        CreateCategoryRequestDto request = new CreateCategoryRequestDto(
                testName, testDescription
        );
        Mockito.when(categoryRepository.save(beforeSave)).thenReturn(afterSave);
        Mockito.when(categoryMapper.toModel(request)).thenReturn(beforeSave);
        Mockito.when(categoryMapper.toResponseDto(afterSave)).thenReturn(expected);
        // when
        CategoryResponseDto actual = categoryService.save(request);
        // then
        assertEquals(expected, actual);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(beforeSave);
        Mockito.verify(categoryRepository, Mockito.times(1)).existsByName(testName);
        Mockito.verify(categoryMapper, Mockito.times(1)).toModel(request);
        Mockito.verify(categoryMapper, Mockito.times(1)).toResponseDto(afterSave);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void save_requestDtoWithoutName_throwDivE() {
        // given
        String testDescription = "Test description";
        Category beforeSave = new Category();
        beforeSave.setName(null);
        beforeSave.setDescription(testDescription);
        beforeSave.setDeleted(false);
        CreateCategoryRequestDto request = new CreateCategoryRequestDto(
                null, testDescription
        );
        Mockito.when(categoryRepository.save(beforeSave))
                .thenThrow(new DataIntegrityViolationException("column `name` cannot be null"));
        Mockito.when(categoryMapper.toModel(request)).thenReturn(beforeSave);
        // when
        assertThrows(DataIntegrityViolationException.class, () -> categoryService.save(request));
        // then
        Mockito.verify(categoryRepository, Mockito.times(1)).save(beforeSave);
        Mockito.verify(categoryRepository, Mockito.times(1)).existsByName(null);
        Mockito.verify(categoryMapper, Mockito.times(1)).toModel(request);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void save_requestDtoWithExistingName_throwDpE() {
        // given
        String testName = "Test name";
        String testDescription = "Test description";
        CreateCategoryRequestDto request = new CreateCategoryRequestDto(
                testName, testDescription
        );
        Mockito.when(categoryRepository.existsByName(testName))
                .thenReturn(true);
        // when
        assertThrows(DataProcessingException.class, () -> categoryService.save(request));
        // then
        Mockito.verify(categoryRepository, Mockito.times(1)).existsByName(testName);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void save_requestDtoWithoutDescription_ok() {
        // given
        String testName = "Test category";
        Category beforeSave = new Category();
        beforeSave.setName(testName);
        beforeSave.setDescription(null);
        beforeSave.setDeleted(false);
        Long id = 1L;
        Category afterSave = new Category();
        afterSave.setId(id);
        afterSave.setName(testName);
        afterSave.setDescription(null);
        afterSave.setDeleted(false);
        CategoryResponseDto expected = new CategoryResponseDto(
                id, testName, null
        );
        CreateCategoryRequestDto request = new CreateCategoryRequestDto(
                testName, null
        );
        Mockito.when(categoryRepository.save(beforeSave)).thenReturn(afterSave);
        Mockito.when(categoryMapper.toModel(request)).thenReturn(beforeSave);
        Mockito.when(categoryMapper.toResponseDto(afterSave)).thenReturn(expected);
        // when
        CategoryResponseDto actual = categoryService.save(request);
        // then
        assertEquals(expected, actual);
        Mockito.verify(categoryRepository, Mockito.times(1)).existsByName(testName);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(beforeSave);
        Mockito.verify(categoryMapper, Mockito.times(1)).toModel(request);
        Mockito.verify(categoryMapper, Mockito.times(1)).toResponseDto(afterSave);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void getById_existingId_ok() {
        // given
        Long id = 1L;
        String testName = "Test category";
        String testDescription = "Test description";
        Category model = new Category();
        model.setId(id);
        model.setName(testName);
        model.setDescription(testDescription);
        model.setDeleted(false);
        CategoryResponseDto expected = new CategoryResponseDto(
                id, testName, testDescription
        );
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(model));
        Mockito.when(categoryMapper.toResponseDto(model)).thenReturn(expected);
        // when
        CategoryResponseDto actual = categoryService.getById(id);
        // then
        assertEquals(expected, actual);
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(id);
        Mockito.verify(categoryMapper, Mockito.times(1)).toResponseDto(model);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void getById_nonExistingId_throwEnfE() {
        // given
        Long id = 1L;
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(id));
        // then
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(id);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void deleteById_existingId_ok() {
        // given
        Long id = 1L;
        Mockito.when(categoryRepository.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(categoryRepository).deleteById(id);
        // when
        categoryService.deleteById(id);
        // then
        Mockito.verify(categoryRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(id);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void deleteById_nonExistingId_throwEnfE() {
        // given
        Long id = 1L;
        Mockito.when(categoryRepository.existsById(id)).thenReturn(false);
        // when
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteById(id));
        // then
        Mockito.verify(categoryRepository, Mockito.times(1)).existsById(id);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void update_existingId_ok() {
        // given
        Long id = 1L;
        String testName = "Test category";
        String testDescription = "Test description";
        String newTestDescription = "New test description";
        Category model = new Category();
        model.setId(id);
        model.setName(testName);
        model.setDescription(testDescription);
        model.setDeleted(false);
        UpdateCategoryDto request = new UpdateCategoryDto(
                id, testName, newTestDescription
        );
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(model));
        Mockito.when(categoryRepository.existsByName(testName)).thenReturn(false);
        Mockito.doNothing().when(categoryMapper).updateModel(model, request);
        Mockito.when(categoryRepository.save(model)).thenReturn(model);
        CategoryResponseDto responseDto = new CategoryResponseDto(
                id, testName, newTestDescription
        );
        Mockito.when(categoryMapper.toResponseDto(model)).thenReturn(responseDto);
        // when
        categoryService.update(id, request);
        // then
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(id);
        Mockito.verify(categoryRepository, Mockito.times(1)).existsByName(testName);
        Mockito.verify(categoryMapper, Mockito.times(1)).updateModel(model, request);
        Mockito.verify(categoryMapper, Mockito.times(1)).toResponseDto(model);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(model);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void update_nonExistingId_throwEnfE() {
        // given
        Long id = 1L;
        String testName = "Test category";
        String newTestDescription = "New test description";
        UpdateCategoryDto request = new UpdateCategoryDto(
                id, testName, newTestDescription
        );
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> categoryService.update(id, request));
        // then
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(id);
        Mockito.verifyNoMoreInteractions(categoryMapper);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void findAll_emptyRepository_ok() {
        // given
        Mockito.when(categoryRepository.findAll(Pageable.unpaged())).thenReturn(Page.empty());
        // when
        Page<CategoryResponseDto> all = categoryService.findAll(Pageable.unpaged());
        // then
        assertNotNull(all);
        int expectedTotalElements = 0;
        long actualTotalElements = all.getTotalElements();
        assertEquals(expectedTotalElements, actualTotalElements);
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll(Pageable.unpaged());
        Mockito.verifyNoMoreInteractions(categoryRepository);
        Mockito.verifyNoMoreInteractions(categoryMapper);
    }

    @Test
    public void findAll_oneElementInRepository_ok() {
        // given
        Category category = new Category();
        long id = 1L;
        category.setId(id);
        String testCategory = "Test category";
        category.setName(testCategory);
        String testDescription = "Test description";
        category.setDescription(testDescription);
        category.setDeleted(false);
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                id, testCategory, testDescription
        );
        Mockito.when(categoryRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(
                java.util.List.of(category)
        ));
        Mockito.when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDto);
        // when
        Page<CategoryResponseDto> all = categoryService.findAll(Pageable.unpaged());
        // then
        assertNotNull(all);
        int expectedTotalElements = 1;
        long actualTotalElements = all.getTotalElements();
        assertEquals(expectedTotalElements, actualTotalElements);
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll(Pageable.unpaged());
        Mockito.verify(categoryMapper, Mockito.times(1)).toResponseDto(category);
        Mockito.verifyNoMoreInteractions(categoryRepository);
        Mockito.verifyNoMoreInteractions(categoryMapper);
    }
}
