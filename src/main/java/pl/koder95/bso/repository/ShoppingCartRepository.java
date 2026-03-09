package pl.koder95.bso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.koder95.bso.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}
