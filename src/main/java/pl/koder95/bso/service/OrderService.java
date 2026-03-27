package pl.koder95.bso.service;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.koder95.bso.dto.CreateOrderRequestDto;
import pl.koder95.bso.dto.OrderItemResponseDto;
import pl.koder95.bso.dto.OrderResponseDto;
import pl.koder95.bso.dto.OrderStatusUpdateDto;

public interface OrderService {
    Page<OrderResponseDto> getAll(Pageable pageable);

    OrderResponseDto create(CreateOrderRequestDto dto);

    OrderResponseDto update(Long id, OrderStatusUpdateDto dto);

    Set<OrderItemResponseDto> getItems(Long orderId);

    OrderItemResponseDto getItem(Long orderId, Long itemId);
}
