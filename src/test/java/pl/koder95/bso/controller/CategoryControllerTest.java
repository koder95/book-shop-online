package pl.koder95.bso.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @BeforeAll
    static void setUp(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    // createCategory(CreateCategoryRequestDto categoryDto)

    @Test
    @WithAnonymousUser
    void createCategory_asGuest_status403() {
        try {
            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name": "Test category"
                                    }
                                    """))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/delete_test_category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_asAdmin_ok() {
        try {
            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name": "Test category"
                                    }
                                    """))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_withExistingNameAsAdmin_status409() {
        try {
            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name": "Test category"
                                    }
                                    """))
                    .andExpect(status().isConflict());
        } catch (Exception e) {
            fail(e);
        }
    }

    // getAll(Pageable pageable)

    @Test
    @WithAnonymousUser
    void getAll_asGuest_status403() {
        try {
            mockMvc.perform(get("/api/categories"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_asAdmin_ok() {
        try {
            mockMvc.perform(get("/api/categories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isNotEmpty())
                    .andExpect(jsonPath("$.number").isNumber())
                    .andExpect(jsonPath("$.size").isNumber())
                    .andExpect(jsonPath("$.totalPages").isNumber())
                    .andExpect(jsonPath("$.totalElements").isNumber())
                    .andExpect(jsonPath("$.numberOfElements").isNumber())
                    .andExpect(jsonPath("$.sort").isMap())
                    .andExpect(jsonPath("$.first").isBoolean())
                    .andExpect(jsonPath("$.last").isBoolean());
        } catch (Exception e) {
            fail(e);
        }
    }

    // getCategoryById(Long id)

    @Test
    @WithAnonymousUser
    void getCategoryById_asGuest_status403() {
        try {
            mockMvc.perform(get("/api/categories/1"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_asAdmin_ok() {
        try {
            mockMvc.perform(get("/api/categories/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Test category"));
        } catch (Exception e) {
            fail(e);
        }
    }

    // updateCategory(Long id, UpdateCategoryDto categoryDto)

    @Test
    @WithAnonymousUser
    void updateCategory_asGuest_status403() {
        try {
            mockMvc.perform(put("/api/categories/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name": "Updated category"
                                    }
                                    """))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_asAdmin_ok() {
        try {
            mockMvc.perform(put("/api/categories/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "id": 1,
                                        "name": "Updated category"
                                    }
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Updated category"));
        } catch (Exception e) {
            fail(e);
        }
    }

    // deleteCategory(Long id)

    @Test
    @WithAnonymousUser
    void deleteCategory_asGuest_status403() {
        try {
            mockMvc.perform(delete("/api/categories/1"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_asAdmin_ok() {
        try {
            mockMvc.perform(delete("/api/categories/1"))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            fail(e);
        }
    }

    // getBooksByCategoryId(Long categoryId, Pageable pageable)

    @Test
    @WithAnonymousUser
    void getBooksByCategoryId_asGuest_status403() {
        try {
            mockMvc.perform(get("/api/categories/1/books"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_asAdmin_ok() {
        try {
            mockMvc.perform(get("/api/categories/1/books"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }
    }
}
