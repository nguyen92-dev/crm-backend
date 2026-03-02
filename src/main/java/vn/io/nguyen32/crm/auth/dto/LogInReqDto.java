package vn.io.nguyen32.crm.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LogInReqDto(
    @NotNull(message = "username không được để trống")
    String username,
    @NotNull(message = "password không được để trống")
    String password
) {
}
