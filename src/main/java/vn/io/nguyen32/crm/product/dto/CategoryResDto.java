package vn.io.nguyen32.crm.product.dto;

public record CategoryResDto(
    Long id,
    String categoryName,
    String description,
    Set<CategorySizeResDto> sizes
) {
}
