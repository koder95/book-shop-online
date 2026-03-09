package pl.koder95.bso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.koder95.bso.dto.CartItemRequestDto;
import pl.koder95.bso.dto.ItemQuantityDto;
import pl.koder95.bso.dto.ShoppingCartResponseDto;
import pl.koder95.bso.service.ShoppingCartService;

@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "User shopping cart management",
        description = "Endpoints for managing the authenticated user's shopping cart"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(
            summary = "Get shopping cart",
            description = "Returns the current shopping cart of the authenticated user,"
                    + " including all cart items."
    )
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @Operation(
            summary = "Add item to cart",
            description = "Adds a new book item to the authenticated user's shopping cart. "
                    + "If the book is already in the cart, the quantity is increased."
    )
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto addItem(@RequestBody @Valid CartItemRequestDto cartItemDto) {
        return shoppingCartService.addItem(cartItemDto);
    }

    @Operation(
            summary = "Update cart item quantity",
            description = "Updates the quantity of an existing cart item. "
                    + "Quantity must be at least 1."
    )
    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto updateItem(@PathVariable Long cartItemId,
                                              @RequestBody @Valid ItemQuantityDto dto) {
        return shoppingCartService.updateItem(cartItemId, dto.quantity());
    }

    @Operation(
            summary = "Remove item from cart",
            description = "Permanently removes the specified item from the authenticated "
                    + "user's shopping cart."
    )
    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteItem(cartItemId);
    }
}
