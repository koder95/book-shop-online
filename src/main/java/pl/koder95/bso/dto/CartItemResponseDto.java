package pl.koder95.bso.dto;

public record CartItemResponseDto(Long id, Long bookId, String bookTitle, Integer quantity) {
}
