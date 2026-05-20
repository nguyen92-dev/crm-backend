package vn.io.nguyen32.crm.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategorySizeReqDto(
    @NotNull
    @NotBlank
    String sizeName,
    @NotNull
    @Min(0)
    @Max(100000)
    Double price
) {
}
