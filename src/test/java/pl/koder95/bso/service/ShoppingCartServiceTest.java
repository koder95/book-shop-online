package pl.koder95.bso.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.koder95.bso.dto.CartItemRequestDto;
import pl.koder95.bso.dto.CartItemResponseDto;
import pl.koder95.bso.dto.ShoppingCartResponseDto;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.factory.ShoppingCartFactory;
import pl.koder95.bso.mapper.CartItemMapper;
import pl.koder95.bso.mapper.ShoppingCartMapper;
import pl.koder95.bso.model.Book;
import pl.koder95.bso.model.CartItem;
import pl.koder95.bso.model.Role;
import pl.koder95.bso.model.RoleName;
import pl.koder95.bso.model.ShoppingCart;
import pl.koder95.bso.model.User;
import pl.koder95.bso.repository.BookRepository;
import pl.koder95.bso.repository.CartItemRepository;
import pl.koder95.bso.repository.ShoppingCartRepository;
import pl.koder95.bso.service.impl.ShoppingCartServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private ShoppingCartFactory shoppingCartFactory;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    void getShoppingCart_withoutAuth_throwIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> shoppingCartService.getShoppingCart());
    }

    @Test
    void getShoppingCart_withAuthNotCreatedYetAsUser_ok() {
        Mockito.when(shoppingCartRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);
        User authenticated = new User();
        authenticated.setId(1L);
        authenticated.setRoles(Set.of(userRole));
        authenticated.setEmail("user@example.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                authenticated, null, authenticated.getAuthorities()
        );
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(authenticated);
        cart.setCartItems(Set.of());
        Mockito.when(shoppingCartFactory.createShoppingCart(authenticated)).thenReturn(cart);
        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                cart.getId(), authenticated.getId(), List.of()
        );
        Mockito.when(shoppingCartRepository.save(cart)).thenReturn(cart);
        Mockito.when(shoppingCartMapper.toResponseDto(Mockito.any())).thenReturn(expected);
        ShoppingCartResponseDto actual;
        try (var mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext mockedSecurityContext = Mockito.mock();
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockedSecurityContext);
            Mockito.when(mockedSecurityContext.getAuthentication()).thenReturn(auth);
            actual = shoppingCartService.getShoppingCart();
            Mockito.verify(mockedSecurityContext, Mockito.times(2)).getAuthentication();
            mockedStatic.verify(SecurityContextHolder::getContext, Mockito.times(2));
            Mockito.verifyNoMoreInteractions(mockedSecurityContext);
            mockedStatic.verifyNoMoreInteractions();
        }
        assertEquals(expected, actual);
        Mockito.verifyNoMoreInteractions(
                shoppingCartRepository, shoppingCartFactory, shoppingCartMapper
        );
        Mockito.verifyNoInteractions(bookRepository, cartItemRepository, cartItemMapper);
    }

    @Test
    void getShoppingCart_withAuthCreatedYetAsAdmin_ok() {
        Role adminRole = new Role();
        adminRole.setName(RoleName.ROLE_ADMIN);
        User authenticated = new User();
        authenticated.setId(1L);
        authenticated.setRoles(Set.of(adminRole));
        authenticated.setEmail("admin@example.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                authenticated, null, authenticated.getAuthorities()
        );
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(authenticated);
        cart.setCartItems(Set.of());
        Mockito.when(shoppingCartRepository.findById(1L)).thenReturn(java.util.Optional.of(cart));
        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                cart.getId(), authenticated.getId(), List.of()
        );
        Mockito.when(shoppingCartMapper.toResponseDto(Mockito.any())).thenReturn(expected);
        ShoppingCartResponseDto actual;
        try (var mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext mockedSecurityContext = Mockito.mock();
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockedSecurityContext);
            Mockito.when(mockedSecurityContext.getAuthentication()).thenReturn(auth);
            actual = shoppingCartService.getShoppingCart();
            Mockito.verify(mockedSecurityContext, Mockito.times(1)).getAuthentication();
            mockedStatic.verify(SecurityContextHolder::getContext, Mockito.times(1));
            Mockito.verifyNoMoreInteractions(mockedSecurityContext);
            mockedStatic.verifyNoMoreInteractions();
        }
        assertEquals(expected, actual);
        Mockito.verifyNoMoreInteractions(shoppingCartRepository, shoppingCartMapper);
        Mockito.verifyNoInteractions(
                shoppingCartFactory, bookRepository, cartItemRepository, cartItemMapper
        );
    }

    @Test
    void addItem_toEmptyCartAsOwner_ok() {
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);
        User authenticated = new User();
        authenticated.setId(1L);
        authenticated.setRoles(Set.of(userRole));
        authenticated.setEmail("user@example.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                authenticated, null, authenticated.getAuthorities()
        );
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(authenticated);
        cart.setCartItems(new HashSet<>());
        Mockito.when(shoppingCartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("author");
        book.setTitle("title");
        book.setIsbn("isbn");
        book.setPrice(BigDecimal.TEN);
        book.setCategories(new HashSet<>());
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        CartItemRequestDto requestDto = new CartItemRequestDto(1L, 1);
        CartItem cartItem = new CartItem();
        Mockito.when(cartItemMapper.toModel(requestDto, book, cart)).thenReturn(cartItem);
        Mockito.when(cartItemRepository.findFirstByShoppingCartAndBook(cart, book))
                .thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        Mockito.when(shoppingCartRepository.save(cart)).thenReturn(cart);
        List<CartItemResponseDto> cartItems = new ArrayList<>();
        cartItems.add(new CartItemResponseDto(1L, 1L, book.getTitle(), 1));
        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                cart.getId(), authenticated.getId(), cartItems
        );
        Mockito.when(shoppingCartMapper.toResponseDto(cart)).thenReturn(expected);
        ShoppingCartResponseDto actual;
        try (var mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext mockedSecurityContext = Mockito.mock();
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockedSecurityContext);
            Mockito.when(mockedSecurityContext.getAuthentication()).thenReturn(auth);
            actual = shoppingCartService.addItem(requestDto);
            Mockito.verify(mockedSecurityContext, Mockito.times(1)).getAuthentication();
            mockedStatic.verify(SecurityContextHolder::getContext, Mockito.times(1));
            Mockito.verifyNoMoreInteractions(mockedSecurityContext);
            mockedStatic.verifyNoMoreInteractions();
        }
        assertEquals(expected, actual);
        Mockito.verifyNoMoreInteractions(
                shoppingCartRepository, shoppingCartMapper,
                cartItemRepository, cartItemMapper, bookRepository
        );
    }

    @Test
    void addItem_toNonEmptyCartAsOwner_ok() {
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);
        User authenticated = new User();
        authenticated.setId(1L);
        authenticated.setRoles(Set.of(userRole));
        authenticated.setEmail("user@example.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                authenticated, null, authenticated.getAuthorities()
        );
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(authenticated);
        cart.setCartItems(new HashSet<>());
        Mockito.when(shoppingCartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("author");
        book.setTitle("title");
        book.setIsbn("isbn");
        book.setPrice(BigDecimal.TEN);
        book.setCategories(new HashSet<>());
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        CartItemRequestDto requestDto = new CartItemRequestDto(1L, 1);
        CartItem cartItem = new CartItem();
        Mockito.when(cartItemMapper.toModel(requestDto, book, cart)).thenReturn(cartItem);
        Mockito.when(cartItemRepository.findFirstByShoppingCartAndBook(cart, book))
                .thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        Mockito.when(shoppingCartRepository.save(cart)).thenReturn(cart);
        List<CartItemResponseDto> cartItems = new ArrayList<>();
        cartItems.add(new CartItemResponseDto(1L, 1L, book.getTitle(), 1));
        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                cart.getId(), authenticated.getId(), cartItems
        );
        Mockito.when(shoppingCartMapper.toResponseDto(cart)).thenReturn(expected);
        ShoppingCartResponseDto actual;
        try (var mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext mockedSecurityContext = Mockito.mock();
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockedSecurityContext);
            Mockito.when(mockedSecurityContext.getAuthentication()).thenReturn(auth);
            actual = shoppingCartService.addItem(requestDto);
            Mockito.verify(mockedSecurityContext, Mockito.times(1)).getAuthentication();
            mockedStatic.verify(SecurityContextHolder::getContext, Mockito.times(1));
            Mockito.verifyNoMoreInteractions(mockedSecurityContext);
            mockedStatic.verifyNoMoreInteractions();
        }
        assertEquals(expected, actual);
        Mockito.verifyNoMoreInteractions(
                shoppingCartRepository, shoppingCartMapper,
                cartItemRepository, cartItemMapper, bookRepository
        );
    }

    @Test
    void updateItem_negativeIdNegativeQuantity_throwEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.updateItem(-1L, -1));
    }

    @Test
    void updateItem_negativeIdZeroQuantity_throwEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.updateItem(-1L, 0));
    }

    @Test
    void updateItem_zeroIdNegativeQuantity_throwEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.updateItem(0L, -1));
    }

    @Test
    void updateItem_nonExistentIdNegativeQuantity_throwEntityNotFoundException() {
        Mockito.when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.updateItem(1L, -1));
        Mockito.verify(cartItemRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void updateItem_nonExistentIdZeroQuantity_throwEntityNotFoundException() {
        Mockito.when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.updateItem(1L, 0));
        Mockito.verify(cartItemRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void updateItem_nonExistentIdOneQuantity_throwEntityNotFoundException() {
        Mockito.when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.updateItem(1L, 1));
        Mockito.verify(cartItemRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void updateItem_existentIdNegativeQuantity_throwIllegalStateException() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        ShoppingCart shoppingCart = new ShoppingCart();
        cartItem.setShoppingCart(shoppingCart);
        Mockito.when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        assertThrows(IllegalStateException.class, () -> shoppingCartService.updateItem(1L, -1));
        Mockito.verify(cartItemRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void updateItem_asOwner_ok() {
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);
        User authenticated = new User();
        authenticated.setId(1L);
        authenticated.setRoles(Set.of(userRole));
        authenticated.setEmail("user@example.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                authenticated, null, authenticated.getAuthorities()
        );
        Book book = new Book();
        book.setId(1L);
        book.setTitle("title");
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(authenticated);
        cart.setCartItems(new HashSet<>());
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(1);
        cartItem.setBook(book);
        cartItem.setShoppingCart(cart);
        cart.getCartItems().add(cartItem);
        Mockito.when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        Mockito.when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(cart));
        List<CartItemResponseDto> cartItems = new ArrayList<>();
        cartItems.add(new CartItemResponseDto(1L, 1L, book.getTitle(), 3));
        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                cart.getId(), authenticated.getId(), cartItems
        );
        Mockito.when(shoppingCartMapper.toResponseDto(cart)).thenReturn(expected);
        ShoppingCartResponseDto actual;
        try (var mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext mockedSecurityContext = Mockito.mock();
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockedSecurityContext);
            Mockito.when(mockedSecurityContext.getAuthentication()).thenReturn(auth);
            actual = shoppingCartService.updateItem(1L, 3);
            Mockito.verify(mockedSecurityContext, Mockito.times(2)).getAuthentication();
            mockedStatic.verify(SecurityContextHolder::getContext, Mockito.times(2));
            Mockito.verifyNoMoreInteractions(mockedSecurityContext);
            mockedStatic.verifyNoMoreInteractions();
        }
        assertEquals(3, cartItem.getQuantity());
        assertEquals(expected, actual);
        Mockito.verify(cartItemRepository).findById(1L);
        Mockito.verify(cartItemRepository).save(cartItem);
        Mockito.verify(shoppingCartRepository).findById(1L);
        Mockito.verify(shoppingCartMapper).toResponseDto(cart);
        Mockito.verifyNoMoreInteractions(
                cartItemRepository, shoppingCartRepository, shoppingCartMapper
        );
        Mockito.verifyNoInteractions(bookRepository, cartItemMapper, shoppingCartFactory);
    }

    @Test
    void updateItem_asOwnerWhenCartMissing_createsCart_ok() {
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);
        User authenticated = new User();
        authenticated.setId(1L);
        authenticated.setRoles(Set.of(userRole));
        authenticated.setEmail("user@example.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                authenticated, null, authenticated.getAuthorities()
        );
        Book book = new Book();
        book.setId(2L);
        book.setTitle("another");
        ShoppingCart itemCart = new ShoppingCart();
        itemCart.setId(1L);
        itemCart.setUser(authenticated);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(2);
        cartItem.setBook(book);
        cartItem.setShoppingCart(itemCart);
        ShoppingCart createdCart = new ShoppingCart();
        createdCart.setId(1L);
        createdCart.setUser(authenticated);
        createdCart.setCartItems(new HashSet<>());
        Mockito.when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        Mockito.when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(shoppingCartFactory.createShoppingCart(authenticated)).thenReturn(createdCart);
        Mockito.when(shoppingCartRepository.save(createdCart)).thenReturn(createdCart);
        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                createdCart.getId(), authenticated.getId(), List.of()
        );
        Mockito.when(shoppingCartMapper.toResponseDto(createdCart)).thenReturn(expected);
        ShoppingCartResponseDto actual;
        try (var mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext mockedSecurityContext = Mockito.mock();
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockedSecurityContext);
            Mockito.when(mockedSecurityContext.getAuthentication()).thenReturn(auth);
            actual = shoppingCartService.updateItem(1L, 5);
            Mockito.verify(mockedSecurityContext, Mockito.times(3)).getAuthentication();
            mockedStatic.verify(SecurityContextHolder::getContext, Mockito.times(3));
            Mockito.verifyNoMoreInteractions(mockedSecurityContext);
            mockedStatic.verifyNoMoreInteractions();
        }
        assertEquals(5, cartItem.getQuantity());
        assertEquals(expected, actual);
        Mockito.verify(cartItemRepository).findById(1L);
        Mockito.verify(cartItemRepository).save(cartItem);
        Mockito.verify(shoppingCartRepository).findById(1L);
        Mockito.verify(shoppingCartFactory).createShoppingCart(authenticated);
        Mockito.verify(shoppingCartRepository).save(createdCart);
        Mockito.verify(shoppingCartMapper).toResponseDto(createdCart);
        Mockito.verifyNoMoreInteractions(
                cartItemRepository, shoppingCartRepository, shoppingCartFactory, shoppingCartMapper
        );
        Mockito.verifyNoInteractions(bookRepository, cartItemMapper);
    }

    @Test
    void deleteItem_negativeId_throwEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.deleteItem(-1L));
    }

    @Test
    void deleteItem_zeroId_throwEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.deleteItem(0L));
    }

    @Test
    void deleteItem_nonExistentId_throwEntityNotFoundException() {
        Mockito.when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.deleteItem(1L));
        Mockito.verify(cartItemRepository).findById(1L);
        Mockito.verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void deleteItem_existentId_ok() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        ShoppingCart shoppingCart = new ShoppingCart();
        cartItem.setShoppingCart(shoppingCart);
        User authenticated = new User();
        shoppingCart.setUser(authenticated);
        authenticated.setId(1L);
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);
        authenticated.setRoles(Set.of(userRole));
        Mockito.when(cartItemRepository.findById(1L)).thenReturn(java.util.Optional.of(cartItem));
        Mockito.doNothing().when(cartItemRepository).deleteById(1L);
        try (var mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext context = Mockito.mock();
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(context);
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    authenticated, null, authenticated.getAuthorities()
            );
            Mockito.when(context.getAuthentication()).thenReturn(authenticationToken);
            shoppingCartService.deleteItem(1L);
            Mockito.verify(context, Mockito.times(1)).getAuthentication();
            Mockito.verifyNoMoreInteractions(context);
            mockedStatic.verify(SecurityContextHolder::getContext, Mockito.times(1));
            mockedStatic.verifyNoMoreInteractions();
        }
        Mockito.verify(cartItemRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(cartItemRepository, Mockito.times(1)).deleteById(1L);
        Mockito.verifyNoMoreInteractions(cartItemRepository);
    }
}
