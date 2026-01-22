package pl.koder95.bso.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.koder95.bso.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
