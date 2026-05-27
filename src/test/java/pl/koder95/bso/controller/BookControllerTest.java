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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    protected static MockMvc mockMvc;

    @BeforeAll
    static void setUp(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithAnonymousUser
    void getAll_asGuest_status403() {
        try {
            mockMvc.perform(get("/api/books")).andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll_asAdmin_ok() {
        try {
            mockMvc.perform(get("/api/books"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.number").isNumber())
                    .andExpect(jsonPath("$.size").isNumber())
                    .andExpect(jsonPath("$.totalPages").isNumber())
                    .andExpect(jsonPath("$.totalElements").isNumber())
                    .andExpect(jsonPath("$.numberOfElements").isNumber())
                    .andExpect(jsonPath("$.sort").isMap())
                    .andExpect(jsonPath("$.first").isBoolean())
                    .andExpect(jsonPath("$.last").isBoolean())
                    .andReturn();
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithAnonymousUser
    void get_nonExistingAsGuest_status403() {
        try {
            mockMvc.perform(get("/api/books/999"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void get_nonExistingAsAdmin_status404() {
        try {
            mockMvc.perform(get("/api/books/999"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithAnonymousUser
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void get_existingAsGuest_status403() {
        try {
            mockMvc.perform(get("/api/books/1"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void get_existingAsAdmin_ok() {
        try {
            mockMvc.perform(get("/api/books/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.title").isString())
                    .andExpect(jsonPath("$.author").isString())
                    .andExpect(jsonPath("$.isbn").isString())
                    .andExpect(jsonPath("$.price").isNumber())
                    .andExpect(jsonPath("$.description").isString())
                    .andExpect(jsonPath("$.coverImage").isString())
                    .andExpect(jsonPath("$.categoryIds").isArray())
                    .andReturn();
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithAnonymousUser
    void create_nonExistingAsGuest_status403() {
        try {
            mockMvc.perform(post("/api/books"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create_nonExistingAsAdmin_ok() {
        try {
            mockMvc.perform(post("/api/books").content("""
                    {
                        "title": "Test Book",
                        "author": "Test Author",
                        "isbn": "1234567890123",
                        "price": 19.99,
                        "description": "A book created for testing purposes.",
                        "coverImage": "test-book.jpg",
                        "categoryIds": []
                    }
                    """).contentType("application/json"))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/sql/insert_test_category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create_nonExistingWithExistingCategoryIdsAsAdmin_ok() {
        try {
            mockMvc.perform(post("/api/books").content("""
                    {
                        "title": "Test Book",
                        "author": "Test Author",
                        "isbn": "1234567890123",
                        "price": 19.99,
                        "description": "A book created for testing purposes.",
                        "coverImage": "test-book.jpg",
                        "categoryIds": [1]
                    }
                    """).contentType("application/json"))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithAnonymousUser
    void create_existingAsGuest_status403() {
        try {
            mockMvc.perform(post("/api/books").content("""
                    {
                        "title": "Test Book",
                        "author": "Test Author",
                        "isbn": "1234567890123",
                        "price": 19.99,
                        "description": "A book created for testing purposes.",
                        "coverImage": "test-book.jpg",
                        "categoryIds": []
                    }
                    """).contentType("application/json"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create_existingAsAdmin_status409() {
        try {
            mockMvc.perform(post("/api/books").content("""
                    {
                        "title": "Test Book",
                        "author": "Test Author",
                        "isbn": "978-0-123456-78-9",
                        "price": 19.99,
                        "description": "A book created for testing purposes.",
                        "coverImage": "test-book.jpg",
                        "categoryIds": []
                    }
                    """).contentType("application/json"))
                    .andExpect(status().isConflict());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithAnonymousUser
    void update_asGuest_status403() {
        try {
            mockMvc.perform(put("/api/books/1"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_asAdmin_ok() {
        try {
            mockMvc.perform(put("/api/books/1").content("""
                    {
                        "title": "Updated Test Book",
                        "author": "Updated Test Author",
                        "isbn": "978-0-123456-78-9",
                        "price": 29.99,
                        "description": "An updated book created for testing purposes.",
                        "coverImage": "updated-test-book.jpg",
                        "categoryIds": []
                    }
                    """).contentType("application/json"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithAnonymousUser
    void delete_asGuest_status403() {
        try {
            mockMvc.perform(delete("/api/books/1"))
                    .andExpect(status().isForbidden());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_existingAsAdmin_ok() {
        try {
            mockMvc.perform(delete("/api/books/1"))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void delete_nonExistingAsAdmin_status404() {
        try {
            mockMvc.perform(delete("/api/books/1"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_byExistingIsbnAsAdmin_ok() {
        try {
            mockMvc.perform(get("/api/books/search?isbns=978-0-123456-78-9"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isNotEmpty())
                    .andExpect(jsonPath("$.number").isNumber())
                    .andExpect(jsonPath("$.size").isNumber())
                    .andExpect(jsonPath("$.totalPages").isNumber())
                    .andExpect(jsonPath("$.totalElements").isNumber())
                    .andExpect(jsonPath("$.numberOfElements").isNumber())
                    .andExpect(jsonPath("$.sort").isMap())
                    .andExpect(jsonPath("$.first").isBoolean())
                    .andExpect(jsonPath("$.last").isBoolean())
                    .andReturn();
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_byNonExistingIsbnAsAdmin_ok() {
        try {
            mockMvc.perform(get("/api/books/search?isbns=999-9-999999-99-9"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.number").isNumber())
                    .andExpect(jsonPath("$.size").isNumber())
                    .andExpect(jsonPath("$.totalPages").isNumber())
                    .andExpect(jsonPath("$.totalElements").isNumber())
                    .andExpect(jsonPath("$.numberOfElements").isNumber())
                    .andExpect(jsonPath("$.sort").isMap())
                    .andExpect(jsonPath("$.first").isBoolean())
                    .andExpect(jsonPath("$.last").isBoolean())
                    .andReturn();
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_byExistingAuthorAsAdmin_ok() {
        try {
            mockMvc.perform(get("/api/books/search?authors=Test Author"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isNotEmpty())
                    .andExpect(jsonPath("$.number").isNumber())
                    .andExpect(jsonPath("$.size").isNumber())
                    .andExpect(jsonPath("$.totalPages").isNumber())
                    .andExpect(jsonPath("$.totalElements").isNumber())
                    .andExpect(jsonPath("$.numberOfElements").isNumber())
                    .andExpect(jsonPath("$.sort").isMap())
                    .andExpect(jsonPath("$.first").isBoolean())
                    .andExpect(jsonPath("$.last").isBoolean())
                    .andReturn();
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "/sql/insert_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete_test_book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_byNonExistingAuthorAsAdmin_ok() {
        try {
            mockMvc.perform(get("/api/books/search?authors=Tester"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.number").isNumber())
                    .andExpect(jsonPath("$.size").isNumber())
                    .andExpect(jsonPath("$.totalPages").isNumber())
                    .andExpect(jsonPath("$.totalElements").isNumber())
                    .andExpect(jsonPath("$.numberOfElements").isNumber())
                    .andExpect(jsonPath("$.sort").isMap())
                    .andExpect(jsonPath("$.first").isBoolean())
                    .andExpect(jsonPath("$.last").isBoolean())
                    .andReturn();
        } catch (Exception e) {
            fail(e);
        }
    }
}
