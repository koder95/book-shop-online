package pl.koder95.bso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.koder95.bso.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
