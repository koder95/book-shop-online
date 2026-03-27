package pl.koder95.bso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.koder95.bso.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
