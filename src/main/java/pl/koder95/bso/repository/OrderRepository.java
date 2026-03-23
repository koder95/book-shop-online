package pl.koder95.bso.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.koder95.bso.model.Order;
import pl.koder95.bso.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser(User user, Pageable pageable);
}
