package vn.io.nguyen32.crm.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CategoryReqDto(
    @NotNull
    @NotBlank
    String name,
    String description,
    Set<CategorySizeReqDto> sizes
) {
}
