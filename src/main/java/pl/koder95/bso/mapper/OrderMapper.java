package pl.koder95.bso.mapper;

import java.math.BigDecimal;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import pl.koder95.bso.config.MapperConfig;
import pl.koder95.bso.dto.CreateOrderRequestDto;
import pl.koder95.bso.dto.OrderResponseDto;
import pl.koder95.bso.dto.OrderStatusUpdateDto;
import pl.koder95.bso.model.Order;
import pl.koder95.bso.model.ShoppingCart;
import pl.koder95.bso.model.Status;

@Mapper(config = MapperConfig.class, uses = {ShoppingCartMapper.class, OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shippingAddress", source = "dto.shippingAddress")
    @Mapping(target = "orderItems", source = "cart.cartItems")
    @Mapping(target = "user", source = "cart.user")
    @Mapping(target = "status", expression = "java(pl.koder95.bso.model.Status.PENDING)")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "total", ignore = true)
    Order toModel(CreateOrderRequestDto dto, ShoppingCart cart);

    @Mapping(target = "status", source = "status", qualifiedByName = "getStatus")
    void updateModel(@MappingTarget Order model, OrderStatusUpdateDto dto);

    @Named("getStatus")
    default Status getStatus(String status) {
        return Status.valueOf(status);
    }

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponseDto toResponseDto(Order order);

    @AfterMapping
    default void finalize(@MappingTarget Order order) {
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> item.setOrder(order));
        }

        BigDecimal total = order.getOrderItems() == null
                ? BigDecimal.ZERO
                : order.getOrderItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
    }
}
