package pl.koder95.bso.service.impl;

import java.util.Optional;
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
import pl.koder95.bso.repository.BookRepository;
import pl.koder95.bso.repository.CartItemRepository;
import pl.koder95.bso.repository.ShoppingCartRepository;
import pl.koder95.bso.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartFactory shoppingCartFactory;

    private ShoppingCart createShoppingCart() {
        ShoppingCart created = shoppingCartFactory.createShoppingCart(getAuthenticatedUser());
        shoppingCartRepository.save(created);
        return created;
    }

    private ShoppingCart getOrCreateShoppingCart() {
        return shoppingCartRepository.findById(getAuthenticatedUser().getId())
                .orElseGet(this::createShoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto getShoppingCart() {
        ShoppingCart shoppingCart = getOrCreateShoppingCart();
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addItem(CartItemRequestDto cartItemDto) {
        Book book = bookRepository.findById(cartItemDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot find a book with id: " + cartItemDto.bookId()
                )
        );
        ShoppingCart shoppingCart = getOrCreateShoppingCart();
        CartItem item = cartItemMapper.toModel(cartItemDto, book, shoppingCart);
        Optional<CartItem> first = cartItemRepository
                .findFirstByShoppingCartAndBook(shoppingCart, book);
        if (first.isPresent()) {
            Integer quantity = item.getQuantity();
            quantity = quantity == null ? 0 : quantity; // preventing NPE
            item = first.get();
            item.setQuantity(item.getQuantity() + quantity);
        }
        cartItemRepository.save(item);
        shoppingCart.getCartItems().add(item);
        ShoppingCart saved = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateItem(Long id, int quantity) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find cart item id: " + id));
        authorizeCartItemAccess(cartItem);
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = getOrCreateShoppingCart();
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isPresent()) {
            authorizeCartItemAccess(cartItem.get());
            cartItemRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Cannot find cart item id: " + id);
        }
    }

    private static void authorizeCartItemAccess(CartItem cartItem) {
        User user = getAuthenticatedUser();
        if (!cartItem.getShoppingCart().getUser().equals(user)) {
            throw new AccessDeniedException(
                    "Item is not in shopping cart maintained by user: " + user.getEmail()
            );
        }
    }

    private static User getAuthenticatedUser() {
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
