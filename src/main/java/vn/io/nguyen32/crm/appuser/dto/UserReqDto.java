package vn.io.nguyen32.crm.appuser.dto;

import jakarta.validation.constraints.NotBlank;

public record UserReqDto(
    @NotBlank
    String username,
    @NotBlank
    String password,
    String email,
    String fullName
) {
}
