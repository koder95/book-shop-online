package pl.koder95.bso.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.koder95.bso.dto.CartItemRequestDto;
import pl.koder95.bso.dto.ShoppingCartResponseDto;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.factory.ShoppingCartFactory;
import pl.koder95.bso.mapper.CartItemMapper;
import pl.koder95.bso.mapper.ShoppingCartMapper;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.model.CartItem;
import pl.koder95.bso.model.ShoppingCart;
import pl.koder95.bso.model.User;
import pl.koder95.bso.repository.CartItemRepository;
import pl.koder95.bso.repository.ShoppingCartRepository;
import pl.koder95.bso.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartFactory shoppingCartFactory;

    private ShoppingCart createShoppingCart() {
        ShoppingCart created = shoppingCartFactory.createShoppingCart(getAuthentication());
        shoppingCartRepository.save(created);
        return created;
    }

    private ShoppingCart getOrCreateShoppingCart() {
        return shoppingCartRepository.findById(getAuthentication().getId())
                .orElseGet(this::createShoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartMapper.toResponseDto(getOrCreateShoppingCart());
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addItem(CartItemRequestDto cartItemDto) {
        CartItem item = cartItemMapper.toModel(cartItemDto);
        ShoppingCart shoppingCart = getOrCreateShoppingCart();
        shoppingCart.setCartItems(normalizeCartItems(item, shoppingCart));
        ShoppingCart saved = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toResponseDto(saved);
    }

    private Set<CartItem> normalizeCartItems(CartItem item, ShoppingCart shoppingCart) {
        HashSet<CartItem> cartItems = new HashSet<>(shoppingCart.getCartItems());
        if (cartItems.contains(item)) {
            item = reduceToOneItem(item.getBook(), cartItems);
        }
        cartItemRepository.save(item);
        cartItems.add(item);
        return cartItems;
    }

    private CartItem reduceToOneItem(Book book, Set<CartItem> cartItems) {
        List<CartItem> cartItemWithTheSameBook = cartItems.stream()
                .filter(cartItem -> cartItem.getBook().equals(book))
                .toList();
        CartItem first = cartItemWithTheSameBook.removeFirst();
        cartItems.remove(first);
        if (!cartItemWithTheSameBook.isEmpty()) {
            List<CartItem> toRemove = new ArrayList<>();
            cartItemWithTheSameBook.forEach(cart -> {
                cartItems.remove(cart);
                toRemove.add(cart);
                first.setQuantity(first.getQuantity() + cart.getQuantity());
            });
            cartItemRepository.deleteAll(toRemove);
        }
        return first;
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateItem(Long id, int quantity) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find cart item id: " + id));
        authorizeCartItemAccess(cartItem);
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toResponseDto(getOrCreateShoppingCart());
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isPresent()) {
            authorizeCartItemAccess(cartItem.get());
            cartItemRepository.deleteById(id);
        }
    }

    private static void authorizeCartItemAccess(CartItem cartItem) {
        User user = getAuthentication();
        if (!cartItem.getShoppingCart().getUser().equals(user)) {
            throw new AccessDeniedException(
                    "Item is not in shopping cart maintained by user: " + user.getEmail()
            );
        }
    }

    private static User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Authentication object is null");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new IllegalStateException("Authentication principal object is an unknown type");
    }
}
