package pl.koder95.bso.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.model.CartItem;
import pl.koder95.bso.model.ShoppingCart;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findFirstByShoppingCartAndBook(ShoppingCart shoppingCart, Book book);
}
