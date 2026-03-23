package pl.koder95.bso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@Table(name = "shopping_carts")
@SQLDelete(sql = "UPDATE shopping_carts SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class ShoppingCart {
    @Id
    private Long id;
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private User user;
    @OneToMany(mappedBy = "shoppingCart")
    private Set<CartItem> cartItems = new HashSet<>();
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;
}
