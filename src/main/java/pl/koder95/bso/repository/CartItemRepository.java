package pl.koder95.bso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.koder95.bso.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
