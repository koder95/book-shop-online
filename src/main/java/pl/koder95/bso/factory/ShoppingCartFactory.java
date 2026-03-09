package pl.koder95.bso.factory;

import pl.koder95.bso.model.ShoppingCart;
import pl.koder95.bso.model.User;

public interface ShoppingCartFactory {
    ShoppingCart createShoppingCart(User user);
}
