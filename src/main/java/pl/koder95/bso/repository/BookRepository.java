package pl.koder95.bso.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.koder95.bso.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>,
                                        JpaSpecificationExecutor<Book> {
    @Query("FROM Book b JOIN FETCH b.categories c WHERE c.id = :categoryId")
    Page<Book> findAllByCategoryId(Long categoryId, Pageable pageable);
}
