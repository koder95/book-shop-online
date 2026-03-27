package pl.koder95.bso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.koder95.bso.dto.CreateOrderRequestDto;
import pl.koder95.bso.dto.OrderItemResponseDto;
import pl.koder95.bso.dto.OrderResponseDto;
import pl.koder95.bso.dto.OrderStatusUpdateDto;
import pl.koder95.bso.service.OrderService;

@SecurityRequirement(name = "bearer-key")
@Tag(name = "Order management", description = "Endpoints for managing customer orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get all orders",
            description = "Retrieve a paginated list of user's orders")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<OrderResponseDto> getOrders(@ParameterObject Pageable pageable) {
        return orderService.getAll(pageable);
    }

    @Operation(summary = "Create a new order",
            description = "Place a new order based on the current shopping cart")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@Valid @RequestBody CreateOrderRequestDto dto) {
        return orderService.create(dto);
    }

    @Operation(summary = "Update order status",
            description = "Allows changing the status of an existing order")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                        @Valid @RequestBody OrderStatusUpdateDto dto) {
        return orderService.update(id, dto);
    }

    @Operation(summary = "Get items by order ID",
            description = "Retrieve all items included in a specific order")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public Set<OrderItemResponseDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getItems(orderId);
    }

    @Operation(summary = "Get a specific order item",
            description = "Retrieve details of a single item from a specific order")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemResponseDto getOrderItem(@PathVariable Long orderId,
                                             @PathVariable Long itemId) {
        return orderService.getItem(orderId, itemId);
    }
}
