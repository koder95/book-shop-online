package pl.koder95.bso.service.impl;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.koder95.bso.dto.CreateOrderRequestDto;
import pl.koder95.bso.dto.OrderItemResponseDto;
import pl.koder95.bso.dto.OrderResponseDto;
import pl.koder95.bso.dto.OrderStatusUpdateDto;
import pl.koder95.bso.exception.CreateOrderException;
import pl.koder95.bso.exception.EntityNotFoundException;
import pl.koder95.bso.mapper.OrderItemMapper;
import pl.koder95.bso.mapper.OrderMapper;
import pl.koder95.bso.model.Order;
import pl.koder95.bso.model.OrderItem;
import pl.koder95.bso.model.ShoppingCart;
import pl.koder95.bso.model.User;
import pl.koder95.bso.repository.CartItemRepository;
import pl.koder95.bso.repository.OrderItemRepository;
import pl.koder95.bso.repository.OrderRepository;
import pl.koder95.bso.repository.ShoppingCartRepository;
import pl.koder95.bso.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public Page<OrderResponseDto> getAll(Pageable pageable) {
        User user = getAuthenticatedUser();
        return orderRepository.findAllByUser(user, pageable)
                .map(orderMapper::toResponseDto);
    }

    @Override
    @Transactional
    public OrderResponseDto create(CreateOrderRequestDto dto) {
        User user = getAuthenticatedUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Cannot find cart for authenticated user")
        );
        authorizeCartAccess(shoppingCart);
        Order order = orderMapper.toModel(dto, shoppingCart);
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new CreateOrderException("Cannot create order without order items. Add a item to "
                    + "shopping cart at first.");
        }
        Order saved = orderRepository.save(order);
        clearCart(shoppingCart);
        return orderMapper.toResponseDto(saved);
    }

    private void clearCart(ShoppingCart shoppingCart) {
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public OrderResponseDto update(Long id, OrderStatusUpdateDto dto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + id)
        );
        orderMapper.updateModel(order, dto);
        return orderMapper.toResponseDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderItemResponseDto> getItems(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + orderId)
        );
        authorizeOrderAccess(order);
        OrderResponseDto orderResponseDto = orderMapper.toResponseDto(order);
        return Set.copyOf(orderResponseDto.orderItems());
    }

    @Override
    public OrderItemResponseDto getItem(Long orderId, Long itemId) {
        OrderItem item = orderItemRepository.findById(itemId).orElseThrow(
                () -> new EntityNotFoundException("Item not found with id: " + itemId)
        );
        Order order = item.getOrder();
        if (!order.getId().equals(orderId)) {
            throw new EntityNotFoundException("The item (#" + itemId + ") is not included "
                    + " in the order (#" + orderId + ")");
        }
        authorizeOrderAccess(order);
        return orderItemMapper.toResponseDto(item);
    }

    private static void authorizeCartAccess(ShoppingCart cart) {
        User user = getAuthenticatedUser();
        if (!cart.getUser().equals(user)) {
            throw new AccessDeniedException(
                    "Cart is not maintained by authenticated user"
            );
        }
    }

    private static void authorizeOrderAccess(Order order) {
        User user = getAuthenticatedUser();
        if (!order.getUser().equals(user)) {
            throw new AccessDeniedException(
                    "Order is not maintained by authenticated user"
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
