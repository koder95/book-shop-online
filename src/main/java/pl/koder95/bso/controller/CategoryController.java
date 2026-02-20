package pl.koder95.bso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.koder95.bso.dto.BookDtoWithoutCategoryIds;
import pl.koder95.bso.dto.CategoryDto;
import pl.koder95.bso.service.BookService;
import pl.koder95.bso.service.CategoryService;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "Category management", description = "Endpoints for managing categories")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(
            summary = "Create a new category",
            description = "Allows admin to create a new book category"
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @Operation(
            summary = "Get all categories",
            description = "Retrieve a paginated list of all available categories"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<CategoryDto> getAll(@ParameterObject Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(
            summary = "Get category by ID",
            description = "Retrieve details of a specific category by its unique identifier"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Update category",
            description = "Update an existing category's information"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto updateCategory(@PathVariable Long id, CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @Operation(summary = "Delete category", description = "Remove a category from the system")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(
            summary = "Get books by category",
            description = "Retrieve a paginated list of books belonging to a specific category"
    )
    @GetMapping("/{id}/books")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id, @ParameterObject Pageable pageable
    ) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
