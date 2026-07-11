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
        Mockito.when(shoppingCartRepository.findById(cart.getId())).thenReturn(java.util.Optional.of(cart));
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("author");
        book.setTitle("title");
        book.setIsbn("isbn");
        book.setPrice(BigDecimal.TEN);
        book.setCategories(new HashSet<>());
        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));
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
}
