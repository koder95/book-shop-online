package pl.koder95.bso.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import pl.koder95.bso.config.JpaTestConfig;
import pl.koder95.bso.model.Role;
import pl.koder95.bso.model.RoleName;
import pl.koder95.bso.model.ShoppingCart;
import pl.koder95.bso.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    private User user;

    @BeforeEach
    void setUp() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("testcontainersInTests");
        newUser.setFirstName("John");
        newUser.setLastName("Smith");
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        newUser.getRoles().add(role);
        user = em.persistAndFlush(newUser);
    }

    @Test
    void save_cartWithExistingUser_sharesIdWithUser() {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        shoppingCartRepository.saveAndFlush(cart);
        em.clear();

        ShoppingCart found = shoppingCartRepository.findById(user.getId()).orElseThrow();
        assertEquals(user.getId(), found.getId());
        assertFalse(found.isDeleted());
    }

    @Test
    void save_cartWithoutUser_throwsException() {
        ShoppingCart cart = new ShoppingCart();

        assertThrows(RuntimeException.class,
                () -> shoppingCartRepository.saveAndFlush(cart));
    }

    @Test
    void delete_cart_isSoftDeleted() {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        shoppingCartRepository.saveAndFlush(cart);

        shoppingCartRepository.deleteById(user.getId());
        shoppingCartRepository.flush();
        em.clear();

        assertTrue(shoppingCartRepository.findById(user.getId()).isEmpty());
    }
}
