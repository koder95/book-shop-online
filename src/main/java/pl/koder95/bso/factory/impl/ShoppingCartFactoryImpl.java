package pl.koder95.bso.factory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.koder95.bso.factory.ShoppingCartFactory;
import pl.koder95.bso.model.ShoppingCart;
import pl.koder95.bso.model.User;

@Component
@RequiredArgsConstructor
public class ShoppingCartFactoryImpl implements ShoppingCartFactory {
    @Override
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCart;
    }
}
